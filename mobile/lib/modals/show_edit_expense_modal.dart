import 'package:flutter/cupertino.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:intl/intl.dart';
import 'package:mobile/dtos/purchase_data.dart';
import 'package:mobile/dtos/requests/installment_conversion_expense_request.dart';
import 'package:mobile/dtos/requests/update_installment_request.dart';
import 'package:mobile/dtos/responses/api_response.dart';
import 'package:mobile/dtos/responses/card_response.dart';
import 'package:mobile/dtos/responses/expenses_response.dart';
import 'package:mobile/models/currency.dart';
import 'package:mobile/models/payment_methods.dart';
import 'package:mobile/services/card_service.dart';
import 'package:mobile/services/category_service.dart';
import 'package:mobile/services/currency_service.dart';
import 'package:mobile/services/expenses_service.dart';
import 'package:mobile/services/payment_methods_service.dart';
import 'package:mobile/theme/colors.dart';
import 'package:mobile/utils/amount_form_field.dart';
import 'package:mobile/utils/date.dart';

import '../dtos/requests/simple_expense_conversion.dart';
import '../dtos/requests/update_simple_expense_request.dart';
import '../dtos/responses/user_category_response.dart';

Future<bool?> showEditExpensesModal(BuildContext context, ExpenseResponse expense) async {
  // Before showing it, we need to check if the expense is of installment type or not.
  // If it is, we need to show the total sum, not the actual stuff
  // Otherwise, show it separately

  return await showModalBottomSheet<bool>(
    context: context,
    builder: (ctx) => ExpenseEditionModal(expense: expense),
  );
}

class ExpenseEditionModal extends StatefulWidget {
  final ExpenseResponse expense;

  const ExpenseEditionModal({super.key, required this.expense});
  @override
  State<StatefulWidget> createState() => _ExpenseEditionState();
}

class _ExpenseEditionState extends State<ExpenseEditionModal> {
  final _currencyService = CurrencyService();
  final _pmService = PaymentMethodsService();
  final _categoryService = CategoryService();
  final _cardService = CardService();
  final _expenseService = ExpensesService();
  late final Future<PurchaseData> _purchaseFuture;
  final _formKey = GlobalKey<FormState>();
  bool _isSendingData = false;
  late bool isInstallmentEligible;
  final _installmentsController = TextEditingController();

  // Variables that hold the data for sending
  int? _selectedCurrencyId;
  PaymentMethod? _selectedPm;
  int? _selectedCategoryId;
  CardResponse? _selectedCard;

  List<CardResponse>? _cards;
  bool loadingCards = false;

  bool isInstallment = false;

  late TextEditingController amountTextController;
  late TextEditingController extraInfoController;
  late DateTime? _dateTime;
  late TextEditingController _dateController;

  @override
  void initState() {
    super.initState();
    extraInfoController = TextEditingController(text: widget.expense.info);
    amountTextController = TextEditingController(
      text: widget.expense.installment == null
          ? widget.expense.amount.toStringAsFixed(2)
          : widget.expense.installment!.amount.toStringAsFixed(2),
    );
    _purchaseFuture = _fetchNecessaryData();
    _dateTime = widget.expense.date;
    _dateController = TextEditingController(
      text: _dateTime != null
          ? DateFormat('yyyy-MM-dd').format(_dateTime!)
          : '',
    );
    if (widget.expense.installment != null) {
      isInstallment = true;
      _installmentsController.text = widget.expense.installment!.splits
          .toString();
    }
  }

  Future<void> _showDateWidget() async {
    await showAndSelectDateUtil(
      context: context,
      currentDateTime: _dateTime,
      onDateSelected: (pickedDate) {
        setState(() {
          _dateTime = pickedDate;
        });
      },
      dateController: _dateController,
    );
  }

  void _onChangedPaymentMethod(PaymentMethod? pm) async {
    setState(() {
      _selectedPm = pm;
    });

    if (pm == null) {
      setState(() {
        loadingCards = false;
        isInstallmentEligible = false;
      });
      return;
    }

    if (pm.description == "CARD") {
      if (_cards == null || _cards!.isEmpty) {
        loadingCards = true;
        _cards = await _cardService.getUserCards();
      }
      setState(() {
        isInstallmentEligible = true;
        loadingCards = false;
      });
    } else {
      setState(() {
        isInstallmentEligible = false;
      });
    }
  }

  /// This function is responsible for showing the widget necessary only
  /// when an installment payment method is eligible
  Widget buildInstallmentColumn() {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        SizedBox(height: 15),
        if (loadingCards)
          Center(child: CircularProgressIndicator())
        else if (_cards == null || _cards!.isEmpty)
          GestureDetector(
            onTap: () {
              // TODO: Assign redirect here
            },
            child: Text(
              "You still need to assign one card. Please, click here!!",
              style: TextStyle(color: Colors.blue),
            ),
          )
        else
          DropdownButtonFormField(
            decoration: InputDecoration(
              labelText: "Select your card",
              border: OutlineInputBorder(),
            ),
            value: _selectedCard,
            items: _cards!.map((c) {
              return DropdownMenuItem(
                value: c,
                child: Text(_cardService.formatCard(c)),
              );
            }).toList(),
            onChanged: (CardResponse? value) {
              setState(() {
                _selectedCard = value;
              });
            },
          ),
        // Installments part
        SizedBox(height: 15),
        Row(
          mainAxisAlignment: MainAxisAlignment.spaceBetween,
          children: [
            Text("Pay in installments?"),
            Switch(
              value: isInstallment,
              onChanged: (value) {
                setState(() {
                  isInstallment = value;
                });
              },
            ),
          ],
        ),
        SizedBox(height: 15),
        if (isInstallment)
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

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: EdgeInsets.only(
        top: 20.0,
        left: 20.0,
        right: 20.0,
        bottom: MediaQuery.of(context).viewInsets.bottom + 20.0,
      ),
      width: double.infinity,
      child: FutureBuilder<PurchaseData>(
        future: _purchaseFuture,
        builder: (context, snapshot) {
          if (snapshot.connectionState == ConnectionState.waiting) {
            return Center(child: CircularProgressIndicator());
          }
          if (snapshot.hasError) {
            print("Error: ${snapshot.error}");
            return Center(
              child: Text("An error has occurred, please, log in again!!"),
            );
          }

          final purchase = snapshot.data!;

          if (_selectedCurrencyId == null && purchase.currencies != null) {
            try {
              _selectedCurrencyId = purchase.currencies!
                  .firstWhere((c) => c.currencyFlag == widget.expense.currency)
                  .id;
            } catch (e) {
              print("Error: ${e.toString()}");
              _selectedCurrencyId = purchase.currencies!.first.id;
            }
          }

          _selectedPm ??= purchase.paymentMethods.firstWhere(
            (pm) => pm.description == widget.expense.paymentMethod,
          );

          _selectedCategoryId ??= purchase.categories!
              .firstWhere((c) => c.categoryName == widget.expense.category)
              .id;


          if (purchase.cards != null) {
            _cards = purchase.cards;
            if (widget.expense.cardDataResponse != null) {
              _selectedCard = _cards?.firstWhere(
                  (c) => c.id == widget.expense.cardDataResponse!.id
              );
            }
          }


          return Form(
            key: _formKey,
            child: SingleChildScrollView(
              child: Column(
                mainAxisAlignment: MainAxisAlignment.start,
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text("Edit your expense"),
                  SizedBox(height: 16),
                  // Currencies
                  DropdownButtonFormField<int>(
                    decoration: InputDecoration(
                      labelText: "Currency",
                      border: OutlineInputBorder(),
                    ),
                    items: purchase.currencies!.map((Currency c) {
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
                      if (value == null) return "Please provide a value!!";
                      return null;
                    },
                    value: _selectedCurrencyId,
                  ),
                  SizedBox(height: 16),
                  // PaymentMethods
                  DropdownButtonFormField<PaymentMethod>(
                    decoration: InputDecoration(
                      labelText: "Payment Method",
                      border: OutlineInputBorder(),
                    ),
                    items: purchase.paymentMethods.map((PaymentMethod pm) {
                      return DropdownMenuItem(
                        value: pm,
                        child: Text(pm.description),
                      );
                    }).toList(),
                    onChanged: (PaymentMethod? value) {
                      _onChangedPaymentMethod(value);
                    },
                    value: _selectedPm,
                    validator: (PaymentMethod? value) {
                      if (value == null) return "Please, provide a value";
                      return null;
                    },
                  ),
                  if (isInstallmentEligible) buildInstallmentColumn(),
                  SizedBox(height: 16),
                  // Categories
                  DropdownButtonFormField<int>(
                    decoration: InputDecoration(
                      labelText: "Category",
                      border: OutlineInputBorder(),
                    ),
                    items: purchase.categories!.map((c) {
                      return DropdownMenuItem(
                        value: c.id,
                        child: Text(c.categoryName),
                      );
                    }).toList(),
                    onChanged: (int? value) {
                      setState(() {
                        _selectedCategoryId = value;
                      });
                    },
                    value: _selectedCategoryId,
                    validator: (value) {
                      if (value == null) return "Please, provide a value!!";
                      return null;
                    },
                  ),
                  SizedBox(height: 15),
                  // Amount
                  AmountFormField(controller: amountTextController),
                  SizedBox(height: 15),
                  TextFormField(
                    controller: extraInfoController,
                    minLines: 3,
                    maxLines: null,
                    keyboardType: TextInputType.multiline,
                    decoration: InputDecoration(
                      labelText: "Extra Info",
                      border: OutlineInputBorder(),
                    ),
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
                      _showDateWidget();
                    },
                  ),
                  SizedBox(height: 15),
                  ElevatedButton(
                    onPressed: () {
                      sendData(widget.expense);
                    },
                    style: ElevatedButton.styleFrom(
                      backgroundColor: AppColors.lilac,
                      foregroundColor: Colors.white,
                      shape: RoundedRectangleBorder(
                        borderRadius: BorderRadius.circular(20),
                      ),
                      padding: const EdgeInsets.symmetric(
                        horizontal: 40,
                        vertical: 20,
                      ),
                    ),
                    child: _isSendingData
                        ? CircularProgressIndicator()
                        : Text("Send"),
                  ),
                ],
              ),
            ),
          );
        },
      ),
    );
  }

  Future<PurchaseData> _fetchNecessaryData() async {
    _isSendingData = true;
    final result = await Future.wait([
      _currencyService.fetchCurrencies(),
      _pmService.fetchPaymentMethods(),
      _categoryService.getAllUserCategories(),
      if (widget.expense.paymentMethod == "CARD") _cardService.getUserCards(),
    ]);
    _isSendingData = false;

    if (widget.expense.paymentMethod == "CARD") {
      isInstallmentEligible = true;
      return PurchaseData(
        currencies: result[0] as List<Currency>,
        categories: result[2] as List<UserCategoryResponse>,
        paymentMethods: result[1] as List<PaymentMethod>,
        cards: result[3] as List<CardResponse>,
      );
    } else {
      isInstallmentEligible = false;
      return PurchaseData(
        currencies: result[0] as List<Currency>,
        categories: result[2] as List<UserCategoryResponse>,
        paymentMethods: result[1] as List<PaymentMethod>,
      );
    }
  }

  Future<void> sendData(ExpenseResponse originalExpense) async {

    ApiResponse? response;

    if (!_formKey.currentState!.validate()) {
      return;
    }

    setState(() {
      _isSendingData = true;
    });

    try {
      // 2. Get common data from controllers
      final double amount = double.tryParse(amountTextController.text)!;
      final DateTime? date = DateTime.tryParse(_dateController.text);
      final String? info =
      extraInfoController.text.isNotEmpty ? extraInfoController.text : null;

      bool wasOriginallyInstallment = widget.expense.installment != null;
      bool isNowInstallment = isInstallment;

      // 4. Handle the 4 scenarios
      if (wasOriginallyInstallment) {

        if (isNowInstallment) {
          // SCENARIO 3: Installment -> Installment (Update)
          print("SCENARIO 3: Updating existing installment");
          //TODO: Create your UpdateInstallmentExpenseRequest DTO
          var request = UpdateInstallmentRequest(
            amount: amount,
            splits: int.tryParse(_installmentsController.text)!,
            cardId: _selectedCard!.id,
            categoryId: _selectedCategoryId!,
            currencyId: _selectedCurrencyId!,
            date: date!,
            description: info,
          );
          response = await _expenseService.updateInstallment(widget.expense.installment!.id, request);

        } else {
          // SCENARIO 1: Installment -> Simple (Conversion)
          print("SCENARIO 1: Converting installment to simple expense");
          //TODO: Create your ConvertInstallmentToSimpleRequest DTO
          var request = SimpleExpenseConversion(
            amount: amount, // This is now the simple amount
            paymentMethodId: _selectedPm!.id, // Use _selectedPm
            categoryId: _selectedCategoryId!,
            currencyId: _selectedCurrencyId!,
            date: DateTime.parse(_dateController.text),
            description: info,
          );
          response = await _expenseService.convertInstallmentToSimple(widget.expense.installment!.id, request);
        }
      } else {
        // --- Original was a SIMPLE expense ---

        if (isNowInstallment) {
          // SCENARIO 2: Simple -> Installment (Conversion)
          print("SCENARIO 2: Converting simple expense to installment");
          // This is what you already started
          var request = InstallmentConversionExpenseRequest(
            expenseId: widget.expense.id,
            amount: amount, // Total amount
            splits: int.tryParse(_installmentsController.text)!,
            cardId: _selectedCard!.id, // Make sure _selectedCard is not null
            categoryId: _selectedCategoryId!,
            currencyId: _selectedCurrencyId!,
            date: date,
            info: info,
            paymentMethodId: int.parse(_installmentsController.text),
          );
          // TODO: Call your service
          response = await _expenseService.convertSimpleExpenseToInstallment(request);

        } else {
          // SCENARIO 4: Simple -> Simple (Update)
          print("SCENARIO 4: Updating existing simple expense");
          // TODO: Create your UpdateSimpleExpenseRequest DTO
          bool splitCondition = widget.expense.installment != null && _installmentsController.text.isNotEmpty &&
              (widget.expense.installment!.splits) != int.tryParse(_installmentsController.text);
          var request = UpdateSimpleExpenseRequest(
            amount: double.tryParse(amountTextController.text) != widget.expense.amount ?
                double.tryParse(amountTextController.text) : null,
            splits: splitCondition? int.tryParse(_installmentsController.text) : null,
            extraInfo: extraInfoController.text,
            paymentMethodId: _selectedPm != null ? _selectedPm!.id : null,
            cardId: _selectedCard != null ? _selectedCard!.id : null,
            date: _dateTime != null ? DateTime.tryParse(_dateController.text) : null,
            currencyId: _selectedCurrencyId,
            categoryId: _selectedCategoryId
          );
          response = await _expenseService.updateSimpleExpense(request, widget.expense.id);
        }
      }

    } catch (e) {

      print("Error sending data: $e");
      if (mounted) {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text("Failed to update expense: ${e.toString()}")),
        );
        setState(() {
          _isSendingData = false;
        });
        Navigator.of(context).pop(false);
        return;
      }
    } finally {

      if (mounted) {
        setState(() {
          _isSendingData = false;
        });
      }
    }

    if (!mounted) return;

    if (!_isSendingData) {

      String message;
      bool update = false;

      if (response == null) {
        message = "Failed to connect to the API!! Please, log in later";
      } else if (response.status >= 200 && response.status < 300) {
        message = "Data was sent successfully!!";
        update = true;
      } else {
        message = "An Error occurred!!";
      }

      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text(message))
      );

      if (update) {
        Navigator.of(context).pop(true);
      }

    }


  }
}
