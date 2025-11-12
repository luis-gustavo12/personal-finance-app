import 'dart:ffi';

import 'package:flutter/material.dart';
import 'package:intl/intl.dart';
import 'package:mobile/dtos/responses/expenses_response.dart';
import 'package:mobile/dtos/responses/user_category_response.dart';
import 'package:mobile/services/category_service.dart';
import 'package:mobile/services/expenses_service.dart';
import 'package:mobile/theme/colors.dart';
import 'package:mobile/utils/app_bar.dart';
import 'package:mobile/modals/show_edit_expense_modal.dart';

import '../modals/expense_creation_modal.dart';

class ExpensesPage extends StatefulWidget {
  const ExpensesPage({super.key});

  Widget build(BuildContext context) {
    return const Placeholder();
  }

  @override
  State<StatefulWidget> createState() => _ExpensesState();
}

class _ExpensesState extends State<ExpensesPage> {
  bool _isLoading = true;
  late List<ExpenseResponse>? _expenses;
  final _expensesService = ExpensesService();

  @override
  void initState() {
    super.initState();
    _getExpenses();
  }

  Future<void> _getExpenses() async {
    _expenses = await _expensesService.getUserExpenses();
    setState(() {
      _isLoading = false;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBarUtil.appBar(
        "Despesas",
        AppColors.lilac,
        AppColors.mainBlue,
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: () {
          showExpensesModalCreation(context);
        },
        child: Icon(Icons.add),
      ),
      body: _isLoading
          ? Center(child: CircularProgressIndicator())
          : _expenses == null
          ? Text("Could not connect to the server!!")
          : _expenses!.isEmpty
          ? Column(
              children: [
                SizedBox(height: 350),
                Center(
                  child: Text(
                    "You have no expenses!!",
                    style: TextStyle(fontWeight: FontWeight.bold),
                  ),
                ),
                SizedBox(height: 200),
                ElevatedButton(onPressed: () {}, child: Icon(Icons.refresh)),
              ],
            )
          : RefreshIndicator(
              onRefresh: _getExpenses,
              child: _showExpensesBody(),
            ),
    );
  }

  Widget _showExpensesBody() {
    return ListView(
      children: [
        SizedBox(height: 8),
        ..._expenses!.map((expense) {
          return Card(
            elevation: 4.0,
            margin: EdgeInsets.symmetric(vertical: 8.0),
            child: Container(
              padding: const EdgeInsets.all(12.0),
              color: Theme.of(context).colorScheme.surfaceVariant,
              child: Column(
                children: [
                  ListTile(
                    title: Text(
                      "${expense.currency}  ${expense.amount.toStringAsFixed(2)}",
                      style: TextStyle(
                        fontWeight: FontWeight.bold,
                        fontSize: 18,
                        color: AppColors.mainGreen,
                      ),
                    ),
                    subtitle: Text(
                      "${DateFormat('yyyy-MM-dd').format(expense.date)} - ${expense.paymentMethod}",
                    ),
                    isThreeLine: false,
                    trailing: IconButton(
                      onPressed: () {
                        showEditExpensesModal(context, expense);
                      },
                      icon: Icon(Icons.edit),
                      color: AppColors.mainBlue,
                    ),
                  ),
                  if (expense.installment != null)
                    Padding(
                      padding: const EdgeInsets.only(
                        left: 16.0,
                        right: 16.0,
                        bottom: 8.0,
                      ),
                      child: Text(
                        "${expense.installment!.splits} splits, total amount ${expense.installment!.amount.toStringAsFixed(2)}",
                        style: TextStyle(
                          fontWeight: FontWeight.bold,
                          fontStyle: FontStyle.italic,
                          color: AppColors.mainBlue,
                        ),
                      ),
                    ),
                  if (expense.info != null && expense.info!.isNotEmpty)
                    Padding(
                      padding: const EdgeInsets.only(
                        left: 16.0,
                        right: 16.0,
                        bottom: 8.0,
                      ),
                      child: Text(expense.info!),
                    ),
                ],
              ),
            ),
          );
        }),
      ],
    );
  }
}
