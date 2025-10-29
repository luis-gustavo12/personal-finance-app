import 'package:flutter/material.dart';
import 'package:intl/intl.dart';
import 'package:mobile/dtos/requests/income_creation_request.dart';
import 'package:mobile/services/currency_service.dart';

import '../dtos/requests/currency_payments_requests.dart';
import '../models/currency.dart';
import '../models/payment_methods.dart';
import '../services/incomes_service.dart';
import '../services/payment_methods_service.dart';

void showIncomeCreationModal(BuildContext context) async {
  showModalBottomSheet(
    context: context,
    isScrollControlled: true,
    builder: (ctx) => IncomeCreationModal(),
  );
}

class IncomeCreationModal extends StatefulWidget {
  const IncomeCreationModal({super.key});

  @override
  State<IncomeCreationModal> createState() => _IncomeCreationModalState();
}

class _IncomeCreationModalState extends State<IncomeCreationModal> {
  final _currencyService = CurrencyService();
  final _paymentMethodsService = PaymentMethodsService();
  final _incomesService = IncomesService();
  final _formKey = GlobalKey<FormState>();
  late final Future<IncomeFetchedDataRequest> _fetchedDataRequest;

  DateTime? _dateTime;

  // Data for handling the form, and sending to the server
  int? _selectedCurrencyId; // Selected currency ID
  int? _selectedPaymentMethodId;
  final TextEditingController _dateController = TextEditingController();
  final TextEditingController _amountController = TextEditingController();
  final TextEditingController _descriptionController = TextEditingController();

  bool _formIsBeingSent = false;

  Future<IncomeFetchedDataRequest> _fetchRequests() async {
    final result = await Future.wait([
      _currencyService.fetchCurrencies(),
      _paymentMethodsService.fetchPaymentMethods(),
    ]);

    final fetchedCurrencies = result[0] as List<Currency>;
    final fetchedPaymentMethods = result[1] as List<PaymentMethods>;

    return IncomeFetchedDataRequest(
      currencies: fetchedCurrencies,
      paymentMethods: fetchedPaymentMethods,
    );
  }

  @override
  void initState() {
    super.initState();
    _fetchedDataRequest = _fetchRequests();
  }

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: EdgeInsets.all(20.0),
      width: double.infinity,
      height: 600,
      child: FutureBuilder(
        future: _fetchedDataRequest,
        builder: (context, snapshot) {
          if (snapshot.connectionState == ConnectionState.waiting) {
            return Center(child: CircularProgressIndicator());
          }
          if (snapshot.hasError) {
            return Text("Error: ${snapshot.error}");
          }

          final data = snapshot.data;
          final currencies = data!.currencies;
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
                        Text("Fill incomes data"),
                        SizedBox(height: 15),
                        // Currencies
                        DropdownButtonFormField<int>(
                          value: _selectedCurrencyId,
                          decoration: InputDecoration(
                            labelText: "Currency",
                            border: OutlineInputBorder(),
                          ),
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
                          validator: (value) {
                            if (value == null) {
                              return "Please, choose a value";
                            }
                            return null;
                          },
                        ),
                        SizedBox(height: 15),
                        // Payment Method
                        DropdownButtonFormField<int>(
                          decoration: InputDecoration(
                            labelText: "Payment Method",
                            border: OutlineInputBorder(),
                          ),
                          value: _selectedPaymentMethodId,
                          items: paymentMethods.map((PaymentMethods pm) {
                            return DropdownMenuItem(
                              child: Text(pm.description),
                              value: pm.id,
                            );
                          }).toList(),
                          onChanged: (int? value) {
                            setState(() {
                              _selectedPaymentMethodId = value;
                            });
                          },
                        ),
                        SizedBox(height: 15),
                        // Date
                        TextFormField(
                          controller: _dateController,
                          decoration: InputDecoration(
                            labelText: "Date",
                            border: OutlineInputBorder(),
                            prefixIcon: Icon(Icons.calendar_today),
                          ),
                          onTap: () {
                            _selectDate(context);
                          },
                          readOnly: true,
                        ),
                        SizedBox(height: 15),
                        // Amount
                        TextFormField(
                          controller: _amountController,
                          decoration: InputDecoration(
                            labelText: "Amount",
                            border: OutlineInputBorder(),
                          ),
                        ),
                        SizedBox(height: 15),
                        // Description
                        TextField(
                          controller: _descriptionController,
                          maxLines: null,
                          minLines: 3,
                          keyboardType: TextInputType.multiline,
                          decoration: InputDecoration(
                            labelText: "Insert your description",
                            border: OutlineInputBorder(),
                          ),

                        ),
                        Row(
                         mainAxisAlignment: MainAxisAlignment.end,
                         children: [
                           ElevatedButton(
                             onPressed: _submitForm,
                             child: _formIsBeingSent
                                 ? CircularProgressIndicator()
                                 : Text("Add"),
                           ),
                         ],
                        )
                      ],
                    ),
                  ),
                ),
              ],
            ),
          );
        },
      ),
    );
  }

  Future<void> _selectDate(BuildContext context) async {
    final DateTime? picked = await showDatePicker(
      context: context,
      initialDate: _dateTime ?? DateTime.now(),
      firstDate: DateTime(2000),
      lastDate: DateTime(2101),
    );
    if (picked != null) {
      setState(() {
        _dateTime = picked;
        _dateController.text = DateFormat('yyyy-MM-dd').format(picked);
      });
    }
  }

  Future<void> _submitForm() async {

    var request = IncomeCreationRequest(_descriptionController.text, currencyId: _selectedCurrencyId!, paymentMethodId: _selectedPaymentMethodId!,
        incomeDate: DateTime.parse(_dateController.text), amount: double.parse(_amountController.text));
    var response = _incomesService.createIncome(request: request);
    print("Response: $response");
  }
}
