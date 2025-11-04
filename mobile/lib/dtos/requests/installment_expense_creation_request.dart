class InstallmentExpenseCreationRequest {
  final int categoryId;
  final double amount;
  final int splits;
  final String description;
  final int paymentMethodId;
  final int cardId;
  final DateTime firstSplitDate;
  final int currencyId;

  InstallmentExpenseCreationRequest({
    required this.categoryId,
    required this.amount,
    required this.splits,
    required this.description,
    required this.paymentMethodId,
    required this.cardId,
    required this.firstSplitDate,
    required this.currencyId,
  });

  Map<String, dynamic> toJson() {
    return {
      'categoryId': categoryId,
      'amount': amount,
      'splits': splits,
      'description': description,
      'paymentMethodId': paymentMethodId,
      'cardId': cardId,
      'firstSplitDate': firstSplitDate.toIso8601String(),
      'currencyId': currencyId,
    };
  }
}