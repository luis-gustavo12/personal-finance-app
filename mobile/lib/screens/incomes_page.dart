import 'dart:convert';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:mobile/dtos/responses/incomes_summary_response.dart';
import 'package:mobile/response_factory.dart';
import 'package:mobile/services/incomes_service.dart';
import 'package:mobile/theme/colors.dart';

class IncomesPage extends StatefulWidget {
  const IncomesPage({super.key});

  @override
  State<StatefulWidget> createState() => _IncomesState();
}

class _IncomesState extends State<IncomesPage> {
  bool _isLoading = true;
  List<IncomesSummaryResponse>? _incomesSummaryResponse;
  final IncomesService _incomesService = IncomesService();

  @override
  void initState() {
    super.initState();
    _fetchIncomesData();
  }

  Future<void> _fetchIncomesData() async {
    try {
      _incomesSummaryResponse = await _incomesService.getIncomesData();
      setState(() {
        _isLoading = false;
      });
    } catch (e) {
      return null;
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        backgroundColor: AppColors.lilac,
        leading: Builder(
          builder: (context) => IconButton(
            onPressed: () {
              Navigator.pushNamed(context, '/home');
            },
            icon: Icon(Icons.arrow_back, color: AppColors.mainBlue, size: 32),
          ),
        ),
        title: Text('Incomes'),
      ),
      body: _isLoading ? CircularProgressIndicator() : _getIncomesBody(),
    );
  }

  Widget _getIncomesBody() {
    if (_incomesSummaryResponse == null || _incomesSummaryResponse!.isEmpty) {
      return Text("Nao achou pae");
    }

    return ListView(
      children: [
        ..._incomesSummaryResponse!.map((incomes) {
          return Card(
            elevation: 4.0,
            margin: const EdgeInsets.symmetric(vertical: 8.0),
            child: Container(
              height: 100,
              padding: const EdgeInsets.all(12.0),
              color: Theme.of(context).colorScheme.surfaceVariant,
              child: Column(
                mainAxisAlignment: MainAxisAlignment.spaceAround,
                children: [
                  Row(
                    mainAxisAlignment: MainAxisAlignment.spaceBetween,
                    children: [
                      Text(
                        "${incomes.currency}  ${incomes.amount.toStringAsFixed(2)}",
                        style: TextStyle(
                          fontWeight: FontWeight.bold,
                          fontSize: 18,
                          color: AppColors.mainGreen,
                        ),
                      ),
                      Text(
                        incomes.paymentForm,
                        style: TextStyle(
                          fontWeight: FontWeight.bold,
                          fontSize: 18,
                        ),
                      ),
                    ],
                  ),
                  Row(
                    mainAxisAlignment: MainAxisAlignment.spaceBetween,
                    children: [
                      Text(
                        incomes.date,
                        style: TextStyle(
                          fontWeight: FontWeight.bold,
                          fontSize: 18,
                        ),
                      ),
                      Text(
                        incomes.extraInfo,
                        style: TextStyle(
                          fontWeight: FontWeight.bold,
                          fontSize: 18,
                        ),
                      ),
                    ],
                  ),
                ],
              ),
            ),
          );
        }).toList(),
      ],
    );
  }
}
