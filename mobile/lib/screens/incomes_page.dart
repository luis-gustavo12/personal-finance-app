import 'dart:convert';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:intl/intl.dart';
import 'package:mobile/dtos/requests/income_update_request.dart';
import 'package:mobile/dtos/responses/incomes_summary_response.dart';
import 'package:mobile/models/currency.dart';
import 'package:mobile/models/payment_methods.dart';
import 'package:mobile/communication_handler.dart';
import 'package:mobile/services/currency_service.dart';
import 'package:mobile/services/incomes_service.dart';
import 'package:mobile/services/payment_methods_service.dart';
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
      return Text("Couldn't connect to server, try again later");
    }

    return ListView(
      children: [
        SizedBox(height: 8),
        ..._incomesSummaryResponse!.map((incomes) {
          return Card(
            elevation: 4.0,
            margin: const EdgeInsets.symmetric(vertical: 8.0),
            child: Container(
              height: 100,
              padding: const EdgeInsets.all(12.0),
              color: Theme.of(context).colorScheme.surfaceVariant,
              child: ListTile(
                title: Text(
                  "${incomes.currency}  ${incomes.amount.toStringAsFixed(2)}",
                  style: TextStyle(
                    fontWeight: FontWeight.bold,
                    fontSize: 18,
                    color: AppColors.mainGreen,
                  ),
                ),
                subtitle: Text(
                  "${incomes.date} - ${incomes.paymentForm}",
                  style: TextStyle(fontSize: 15),
                ),
                trailing: IconButton(
                  onPressed: () async {
                    final result = await showModalBottomSheet<bool>(
                      context: context,
                      isScrollControlled: true,
                      builder: (ctx) => Padding (
                        padding: EdgeInsets.only(
                          bottom: MediaQuery.of(ctx).viewInsets.bottom,
                        ),
                        child: FractionallySizedBox(
                          heightFactor: 0.75,
                          child: _showEditModal(incomes),
                        ),
                      )
                    );
                    if (result == true) {
                      _fetchIncomesData();
                    }
                  },
                  icon: Icon(Icons.edit, color: Colors.black),
                ),
                isThreeLine: false,
              ),
            ),
          );
        }).toList(),
      ],
    );
  }

  Widget _showEditModal(IncomesSummaryResponse incomes) {
    return EditIncomeModal(response: incomes);
  }
}

class EditModalRequest {
  final List<Currency> currencies;
  final List<PaymentMethods> paymentMethods;

  EditModalRequest({required this.currencies, required this.paymentMethods});
}

class EditIncomeModal extends StatefulWidget {
  final IncomesSummaryResponse response;

  const EditIncomeModal({super.key, required this.response});

  @override
  State<StatefulWidget> createState() => _EditIncomesModalState(response);
}

class _EditIncomesModalState extends State<EditIncomeModal> {
  late Future<List<Currency>> _currencies;
  final _currencyService = CurrencyService();
  final IncomesSummaryResponse response;
  final _paymentMethodsService = PaymentMethodsService();
  late Future<EditModalRequest> _editModalData;
  final _incomesService = IncomesService();

  // Form keys and controllers
  final _formKey = GlobalKey<FormState>();
  late TextEditingController _dateControllerText;
  late TextEditingController _amountController;
  late TextEditingController _descriptionController;

  // Variables related to the forms
  int? _selectedCurrencyId;
  String? _incomeCurrency;
  String? _selectedPaymentMethodId;
  DateTime? _selectedDate;
  String? _selectedExtraInfo;

  DateTime? _dateTime;

  var _isSubmitting = false;


  _EditIncomesModalState(this.response);


  Future<EditModalRequest> _fetchRequests() async {
    final result = await Future.wait([
      _currencyService.fetchCurrencies(),
      _paymentMethodsService.fetchPaymentMethods(),
    ]);

    final fetchedCurrencies = result[0] as List<Currency>;
    final fetchedPaymentMethods = result[1] as List<PaymentMethods>;

    return EditModalRequest(
      currencies: fetchedCurrencies,
      paymentMethods: fetchedPaymentMethods,
    );
  }

  @override
  void initState() {
    super.initState();
    final response = widget.response;
    _dateControllerText = TextEditingController();
    _amountController = TextEditingController(text: response.amount.toStringAsFixed(2));
    _descriptionController = TextEditingController(text: response.extraInfo);
    if (response.date != null) {
      _dateTime = DateTime.tryParse(response.date);
      if (_dateTime != null) {
        _dateControllerText.text = DateFormat('dd-MM-yyyy').format(_dateTime!);
      }
    }
    _editModalData = _fetchRequests();
    _editModalData.then((data) {
      if (mounted) {
        setState(() {
          try {
            _selectedCurrencyId = data.currencies
                .firstWhere((c) => c.currencyFlag == response.currency)
                .id;
          } catch (e) {
            _selectedCurrencyId = null;
          }
          try {
            final selectedMethod = data.paymentMethods
                .firstWhere((pm) => pm.description == response.paymentForm);
            _selectedPaymentMethodId = selectedMethod.id.toString();
          } catch (e) {
            _selectedPaymentMethodId = null;
          }
        });
      }
    });
  }

  @override
  void dispose() {
    super.dispose();
    _amountController.dispose();
    _dateControllerText.dispose();
    _descriptionController.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.transparent,
      body: Container(
        padding: EdgeInsets.all(20.0),
        width: double.infinity,
        child: FutureBuilder(
          future: _editModalData,
          builder: (context, snapshot) {
            if (snapshot.connectionState == ConnectionState.waiting) {
              return Center(child: CircularProgressIndicator());
            }
            if (snapshot.hasError) {
              return Text("Error: ${snapshot.error}");
            }
            if (!snapshot.hasData) {
              return const Center(child: Text('No currencies found.'));
            }
            final data = snapshot.data!;
            final currencies = data.currencies;
            final paymentMethods = data.paymentMethods;

            return Form(
              key: _formKey,
              child: Column(
                children: [
                  Expanded(
                    child: SingleChildScrollView(
                      child: Column(
                        mainAxisAlignment: MainAxisAlignment.start,
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          Text(
                            "Edit your income",
                            style: Theme.of(context).textTheme.titleLarge,
                          ),
                          SizedBox(height: 15),
                          // Get the currencies available
                          DropdownButtonFormField<int>(
                            decoration: InputDecoration(
                              labelText: "Currency",
                              border: OutlineInputBorder(),
                            ),
                            value: _selectedCurrencyId,
                            items: currencies.map((Currency currency) {
                              return DropdownMenuItem(
                                value: currency.id,
                                child: Text(currency.currencyFlag),
                              );
                            }).toList(),
                            onChanged: (int? value) {
                              setState(() {
                                _selectedCurrencyId = value;
                              });
                            },
                            validator: (value) => value == null ? "Please, select a currency" : null,
                          ),
                          SizedBox(height: 15),
                          // Amount
                          TextFormField(
                            controller: _amountController,
                            decoration: InputDecoration(labelText: "Amount"),
                            validator: (value) {
                              if (value == null || value.isEmpty || double.tryParse(value) == null) {
                                return "Please, select a decimal format";
                              }
                              return null;
                            },
                          ),
                          SizedBox(height: 15,),
                          // Payment Method Id
                          DropdownButtonFormField<String>(
                            decoration: InputDecoration(
                              labelText: "Payment method",
                              border: OutlineInputBorder(),
                            ),
                            value: _selectedPaymentMethodId,
                            items: paymentMethods.map((PaymentMethods paymentMethod) {
                              return DropdownMenuItem(
                                value: paymentMethod.id.toString(),
                                child: Text(paymentMethod.description),
                              );
                            }).toList(),
                            onChanged: (String? value) {
                              setState(() {
                                _selectedPaymentMethodId = value;
                              });
                            },
                            validator: (value) {
                              if (value == null) {
                                return "Please, choose a valid option!";
                              }
                              return null;
                            },
                          ),
                          SizedBox(height: 15,),
                          // Date
                          TextFormField(
                            controller: _dateControllerText,
                            decoration: InputDecoration(
                              labelText: "Date",
                              border: OutlineInputBorder(),
                              prefixIcon: Icon(Icons.calendar_today)
                            ),
                            onTap: (){_selectDate(context);},
                            readOnly: true,

                          ),
                          SizedBox(height: 15,),
                          // Description
                          TextField(
                            controller: _descriptionController,
                            maxLines: null,
                            minLines: 3,
                            keyboardType: TextInputType.multiline,
                            decoration: InputDecoration(
                              hintText: response.extraInfo,
                              border: OutlineInputBorder(),
                              labelText: response.extraInfo
                            ),
                          ),
                          SizedBox(height: 15,),
                          ElevatedButton(
                            onPressed: _isSubmitting? null : _submitForm,
                            style: ElevatedButton.styleFrom(
                              maximumSize: Size(double.infinity, 50),
                            ),
                            child: _isSubmitting ? const SizedBox(
                              height: 20,
                              width: 20,
                              child: CircularProgressIndicator(strokeWidth: 2, color: Colors.white),
                            ) :
                            const Text("Edit"),
                          ),
                        ],
                      ),
                    ),
                  ),
                ],
              ),
            );
          },
        ),
      ),
    );
  }

  Future<void> _selectDate(BuildContext context) async{
    final DateTime? picked = await showDatePicker(
        context: context,
        initialDate: _dateTime ?? DateTime.now(),
        firstDate: DateTime(2000),
        lastDate: DateTime(2101)
    );
    if (picked != null) {
      setState(() {
        _dateTime = picked;
        _dateControllerText.text = DateFormat('yyyy-MM-dd').format(picked);
      });
    }
  }

  Future<void> _submitForm() async {
    if (!_formKey.currentState!.validate()) {
      return;
    }
    setState(() {
      _isSubmitting = true;
    });
    try {
      var request = IncomeUpdateRequest(currencyId: _selectedCurrencyId!, incomeAmount: double.tryParse(_amountController.text)! ,
          paymentMethodId: int.tryParse(_selectedPaymentMethodId!)!, date: _dateTime!, incomeDescription: _descriptionController.text);
      bool result = await _incomesService.updateIncome(request: request, incomeId: response.id);
      if (mounted) {
        if (result) {
          Navigator.of(context).pop(true);
          _showFeedbackSnackBar("Income updated successfully", isError: false);
        } else {
          _showFeedbackSnackBar("Failed to update income. Please try again.", isError: true);
        }
      }

    } catch (e) {
      if (mounted) {
        _showFeedbackSnackBar("Error to update income ${e.toString()}", isError: true);
      }
    } finally {
      if (mounted) {
        setState(() {
          _isSubmitting = false;
        });
      }
    }
  }

  void _showFeedbackSnackBar(String message, {required bool isError}) {
    ScaffoldMessenger.of(context).showSnackBar(
      SnackBar(
        content: Text(message),
        backgroundColor: isError ? Colors.redAccent : Colors.green,
        behavior: SnackBarBehavior.floating,
      ),
    );
  }

}
