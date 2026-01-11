class InstallmentConversionExpenseRequest {
  final int expenseId;
  final double amount;
  final String? info;
  final int? currencyId;
  final int? splits;
  final DateTime? date;
  final int? categoryId;
  final int? cardId;
  final int paymentMethodId;

  const InstallmentConversionExpenseRequest({
    required this.expenseId,
    required this.amount,
    this.info,
    this.currencyId,
    this.splits,
    this.date,
    this.categoryId,
    this.cardId,
    required this.paymentMethodId,
  });

  factory InstallmentConversionExpenseRequest.fromJson(Map<String, dynamic> json) {
    return InstallmentConversionExpenseRequest(
      expenseId: json['expenseId'] as int,
      amount: json['amount'] as double,
      info: json['info'] as String?,
      currencyId: json['currencyId'] as int?,
      date: json['date'] != null ? DateTime.parse(json['date'] as String) : null,
      categoryId: json['categoryId'] as int?,
      cardId: json['cardId'] as int?,
      paymentMethodId: json['paymentMethodId'] as int,
      splits: json['splits'] as int
    );
  }

  Map<String, dynamic> toJson() {
    return <String, dynamic>{
      'expenseId': expenseId,
      'paymentMethodId': paymentMethodId,
      'amount': amount,
      'extraInfo': info,
      'splits': splits,
      'currencyId': currencyId,
      'date': date?.toIso8601String(),
      'categoryId': categoryId,
      'cardId': cardId,
    };
  }
}