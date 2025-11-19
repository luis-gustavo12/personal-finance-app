import 'dart:ffi';

import 'package:flutter/material.dart';
import 'package:intl/intl.dart';
import 'package:mobile/dtos/responses/api_response.dart';
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
                    trailing: !expense.isSubscription ?
                      Row(
                        mainAxisSize: MainAxisSize.min,
                        children: [
                          IconButton(
                            onPressed: () async {
                              bool? result = await showEditExpensesModal(context, expense);
                              if (result == true) _getExpenses();
                            },
                            icon: Icon(Icons.edit),
                            color: AppColors.mainBlue,
                          ),
                          IconButton(
                            onPressed: ()  {
                              _deleteExpense(expense.id, expense);
                            },
                            icon: Icon(Icons.delete),
                            color: Colors.redAccent,
                          ),
                        ],
                      ) : null // For the else case, could be a helper, indicating this is an subscription expense, thus can't be
                              // deleted or edited here
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

  Future<void> _deleteExpense(int id, ExpenseResponse expense) async {

    showDialog(context: context, builder: (BuildContext context) {
      return Center(child: CircularProgressIndicator(),);
    });

    ApiResponse? response;

    try {
      if (expense.installment == null) {
         response = await _expensesService.deleteExpense(id);
      } else {
        response = await _expensesService.deleteInstallmentExpense(expense.installment!.id);
      }

      if (!mounted) return;

      Navigator.of(context).pop();

      if (response == null) throw Exception();

      bool success = response.status >= 200 && response.status < 300;

      if (success) {
        setState(() {
          _expenses?.remove(expense);
        });
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(
            content: Text("Expense deleted successfully!"),
            backgroundColor: Colors.green,
          ),
        );
      } else {
        String errorMessage = "Failed to delete expense.";
        if (response.data is String) {
          errorMessage = response.data;
        } else if (response.data is Map && response.data.containsKey('message')) {
          errorMessage = response.data['message'];
        }
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(
            content: Text(errorMessage),
            backgroundColor: Colors.red,
          ),
        );
      }

    } on Exception catch (e) {
      if (!mounted) return;
      Navigator.of(context).pop();
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(
          content: Text("An error occurred: ${e.toString()}"),
          backgroundColor: Colors.red,
        ),
      );
    }


  }
}
