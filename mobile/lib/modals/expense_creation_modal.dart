import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:intl/intl.dart';
import 'package:mobile/dtos/requests/currency_payments_requests.dart';
import 'package:mobile/dtos/requests/expense_creation_request.dart';
import 'package:mobile/dtos/requests/installment_expense_creation_request.dart';
import 'package:mobile/dtos/responses/api_response.dart';
import 'package:mobile/dtos/responses/card_response.dart';
import 'package:mobile/dtos/responses/user_category_response.dart';
import 'package:mobile/models/currency.dart';
import 'package:mobile/models/payment_methods.dart';
import 'package:mobile/services/card_service.dart';
import 'package:mobile/services/category_service.dart';
import 'package:mobile/services/currency_service.dart';
import 'package:mobile/services/expenses_service.dart';
import 'package:mobile/services/payment_methods_service.dart';
import 'package:mobile/theme/colors.dart';
import 'package:mobile/utils/amount_form_field.dart';
import 'package:mobile/utils/currency_input_formatter.dart';
import 'package:mobile/utils/date_time.dart';

void showExpensesModalCreation(BuildContext context) async {
  showModalBottomSheet(
    context: context,
    isScrollControlled: true,
    builder: (ctx) => ExpenseCreationModal(),
  );
}

class ExpenseCreationModal extends StatefulWidget {
  @override
  State<StatefulWidget> createState() => _ExpenseCreationModalState();
}

class _ExpenseCreationModalState extends State<ExpenseCreationModal> {
  final _currencyService = CurrencyService();
  final _paymentMethodsService = PaymentMethodsService();
  late final Future<TransactionRequiredDataRequest> _fetchedRequests;
  int? _selectedCurrencyId;
  PaymentMethod? _selectedPaymentMethod;
  final _dateController = TextEditingController();
  DateTime? _dateTime;
  bool _cardPmSelected = false;

  final _categoryService = CategoryService();
  late List<UserCategoryResponse>? _userCategories;
  int? _selectedCategoryId;

  // Controllers
  final _amountTextController = TextEditingController();
  final _cardService = CardService();
  final extraInfoController = TextEditingController();

  // Cards related variables
  List<CardResponse>? _cards;
  bool _isLoadingCards = false;
  bool _isInstallment = false;

  CardResponse? _selectedCard;
  int? _selectedCardId;
  final _installmentsController = TextEditingController();

  final _expensesService = ExpensesService();

  final _formKey = GlobalKey<FormState>();

  Future<TransactionRequiredDataRequest> _fetchData() async {
    final result = await Future.wait([
      _currencyService.fetchCurrencies(),
      _paymentMethodsService.fetchPaymentMethods(),
      _categoryService.getAllUserCategories(),
    ]);

    final currencies = result[0] as List<Currency>;
    final paymentMethods = result[1] as List<PaymentMethod>;
    _userCategories = result[2] as List<UserCategoryResponse>;

    return TransactionRequiredDataRequest(
      currencies: currencies,
      paymentMethods: paymentMethods,
    );
  }

  @override
  void initState() {
    super.initState();
    _fetchedRequests = _fetchData();
  }

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: EdgeInsets.all(20.0),
      width: double.infinity,
      height: 600,
      child: FutureBuilder(
        future: _fetchedRequests,
        builder: (context, snapshot) {
          if (snapshot.connectionState == ConnectionState.waiting) {
            return Center(child: CircularProgressIndicator());
          }

          if (snapshot.hasError) {
            return Center(child: Text("Error: ${snapshot.error}"));
          }

          final data = snapshot.data;
          final currencies = data!.currencies;
          final paymentMethods = data.paymentMethods;

          if (currencies.isEmpty || paymentMethods.isEmpty) {
            return Text("Sorry, error occurred!!");
          }

          return Form(
            key: _formKey,
            child: SingleChildScrollView(
              child: Column(
                mainAxisAlignment: MainAxisAlignment.start,
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text("Fill your expenses"),
                  SizedBox(height: 16),
                  // currencies
                  DropdownButtonFormField<int>(
                    decoration: InputDecoration(
                      labelText: "Currency",
                      border: OutlineInputBorder(),
                    ),
                    value: _selectedCurrencyId,
                    items: currencies.map((Currency c) {
                      return DropdownMenuItem(
                        value: c.id,
                        child: Text(c.currencyFlag),
                      );
                    }).toList(),
                    onChanged: (int? value) {
                      setState(() {
                        _selectedCurrencyId = value;
                      });
                    },
                    validator: (int? value) {
                      if (value == null) {
                        return "Please, provide a value!!";
                      }
                      return null;
                    },
                  ),
                  // Payment methods
                  SizedBox(height: 16),
                  DropdownButtonFormField<PaymentMethod>(
                    decoration: InputDecoration(
                      labelText: "Payment Method",
                      border: OutlineInputBorder(),
                    ),
                    value: _selectedPaymentMethod,
                    items: paymentMethods.map((PaymentMethod pm) {
                      return DropdownMenuItem(
                        value: pm,
                        child: Text(pm.description),
                      );
                    }).toList(),
                    onChanged: (PaymentMethod? value) {
                      _onChangedPaymentMethods(value);
                    },
                    validator: (value) {
                      if (value == null) {
                        return "Please, provide a value!!";
                      }
                      return null;
                    },
                  ),
                  if (_cardPmSelected) _buildCardsColumn(),

                  SizedBox(height: 15),
                  // Categories
                  if (_userCategories != null)
                    DropdownButtonFormField<int>(
                      items: _userCategories!.map((category) {
                        return DropdownMenuItem(
                          value: category.id,
                          child: Text(category.categoryName),
                        );
                      }).toList(),
                      onChanged: (int? value) {
                        setState(() {
                          _selectedCategoryId = value;
                        });
                      },
                      value: _selectedCategoryId,
                      decoration: InputDecoration(
                        labelText: "Choose your category",
                        border: OutlineInputBorder(),
                      ),
                      validator: (value) {
                        if (value == null) return "Please, provide a category";
                        return null;
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
                      _showAndSelectDate(context);
                    },
                    validator: (value) {
                      if (value == null || value.isEmpty) {
                        return "Please, provide a value!!";
                      }
                      return null;
                    },
                  ),
                  SizedBox(height: 15),
                  // Amount
                  AmountFormField(controller: _amountTextController),
                  SizedBox(height: 15),
                  // Description/extra info
                  TextField(
                    controller: extraInfoController,
                    minLines: 3,
                    maxLines: null,
                    keyboardType: TextInputType.multiline,
                    decoration: InputDecoration(
                      labelText: "Insert some extra info",
                      border: OutlineInputBorder(),
                    ),
                  ),
                  SizedBox(height: 15),
                  ElevatedButton(
                    onPressed: _sendData,
                    style: ElevatedButton.styleFrom(
                      backgroundColor: AppColors.lilac,
                      foregroundColor: Colors.white,
                      shape: RoundedRectangleBorder(
                        borderRadius: BorderRadius.circular(
                          20,
                        ), // Rounds the corners
                      ),
                      padding: const EdgeInsets.symmetric(
                        horizontal: 40,
                        vertical: 20,
                      ),
                    ),
                    child: Text("Send"),
                  ),
                  SizedBox(height: 15),
                ],
              ),
            ),
          );
        },
      ),
    );
  }

  void _onChangedPaymentMethods(PaymentMethod? paymentMethod) async {
    setState(() {
      _selectedPaymentMethod = paymentMethod;
    });
    if (paymentMethod == null)
      return;
    else if (paymentMethod.description == "CARD") {
      setState(() {
        _cardPmSelected = true;
        _isLoadingCards = true;
      });
      _cards = await _cardService.getUserCards();
      setState(() {
        _isLoadingCards = false;
      });
    } else {
      setState(() {
        _cardPmSelected = false;
      });
    }
  }

  Future<void> _showAndSelectDate(context) async {
    final DateTime? picked = await selectDate(context, initialDate: _dateTime);

    if (picked != null && picked != _dateTime) {
      setState(() {
        _dateTime = picked;
        _dateController.text = DateFormat('yyyy-MM-dd').format(picked);
      });
    }
  }

  Widget _buildCardsColumn() {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        SizedBox(height: 16),
        if (_isLoadingCards)
          Center(child: CircularProgressIndicator())
        else if (_cards == null)
          Text(
            "Sorry, for a card payment, you need to create one",
          ) // TODO: Assign a proper flow
        else if (_cards!.isNotEmpty)
          DropdownButtonFormField<int>(
            decoration: InputDecoration(
              labelText: "Select your card",
              border: OutlineInputBorder(),
            ),
            value: _selectedCardId,
            items: _cards!.map((CardResponse card) {
              return DropdownMenuItem(
                value: card.id,
                child: Text(_cardService.formatCard(card)),
              );
            }).toList(),
            onChanged: (int? value) {
              setState(() {
                _selectedCardId = value;
                if (value == null) {
                  _selectedCard = null;
                } else {
                  _selectedCard = _cards!.firstWhere(
                    (card) => card.id == value,
                  );
                }
              });
            },
          ),
        SizedBox(height: 16),
        Row(
          mainAxisAlignment: MainAxisAlignment.spaceBetween,
          children: [
            Text("Pay in installments?"),
            Switch(
              value: _isInstallment,
              onChanged: (value) {
                setState(() {
                  _isInstallment = value;
                });
              },
            ),
            SizedBox(height: 16),
          ],
        ),
        if (_isInstallment)
          Padding(
            padding: const EdgeInsets.only(top: 8.0),
            child: TextFormField(
              controller: _installmentsController,
              decoration: InputDecoration(
                labelText: "Number of Installments",
                border: OutlineInputBorder(),
              ),
              keyboardType: TextInputType.number,
              inputFormatters: [FilteringTextInputFormatter.digitsOnly],
              validator: (value) {
                if (value == null || value.isEmpty) {
                  return "Please provide number of installments";
                }
                if (int.tryParse(value)! <= 1) {
                  return "Must be more than 1";
                }
                return null;
              },
            ),
          ),
      ],
    );
  }

  Future<void> _sendData() async {
    if (!_formKey.currentState!.validate()) {
      return;
    }

    ApiResponse? response;

    if (_isInstallment) {
      final request = InstallmentExpenseCreationRequest(
        categoryId: _selectedCategoryId!,
        amount: double.tryParse(_amountTextController.text)!,
        splits: int.tryParse(_installmentsController.text)!,
        description: extraInfoController.text,
        paymentMethodId: _selectedPaymentMethod!.id,
        cardId: _selectedCardId!,
        firstSplitDate: DateTime.parse(_dateController.text),
        currencyId: _selectedCurrencyId!,
      );

      response = await _expensesService.sendInstallmentExpense(request);

    } else {
      final request = ExpenseCreationRequest(
        paymentMethodId: _selectedPaymentMethod!.id,
        currencyId: _selectedCurrencyId!,
        amount: double.tryParse(_amountTextController.text)!,
        extraInfo: extraInfoController.text,
        expenseDate: DateTime.parse(_dateController.text),
        categoryId: _selectedCategoryId!,
        cardId: _selectedCardId,
      );
      response = await _expensesService.sendSimpleExpense(request);
    }

    if (response == null || response.status != 200) {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(
          content: Text("Sorry, an error occurred!!"),
          backgroundColor: Colors.redAccent,
        ),
      );
      return;
    }

    ScaffoldMessenger.of(
      context,
    ).showSnackBar(SnackBar(content: Text("Expense created successfully!!")));

    print("Response: $response");
  }
}
