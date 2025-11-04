class ExpenseCreationRequest {
  final int paymentMethodId;
  final int currencyId;
  final double amount;
  final String extraInfo;
  final DateTime expenseDate;
  final int categoryId;
  final int? cardId;

  ExpenseCreationRequest({
    required this.paymentMethodId,
    required this.currencyId,
    required this.amount,
    required this.extraInfo,
    required this.expenseDate,
    required this.categoryId,
    this.cardId,
  });

  Map<String, dynamic> toJson() {
    return {
      'paymentMethodId': paymentMethodId,
      'currencyId': currencyId,
      'amount': amount,
      'extraInfo': extraInfo,
      'date': expenseDate.toIso8601String(),
      'categoryId': categoryId,
      if (cardId != null) 'cardId': cardId,
    };
  }

}