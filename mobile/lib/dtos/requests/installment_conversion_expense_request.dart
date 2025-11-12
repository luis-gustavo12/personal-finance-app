class InstallmentConversionExpenseRequest {
  final int originalExpenseId;
  final double amount;
  final String? info;
  final int? currencyId;
  final int? splits;
  final DateTime? date;
  final int? categoryId;
  final int? cardId;

  const InstallmentConversionExpenseRequest({
    required this.originalExpenseId,
    required this.amount,
    this.info,
    this.currencyId,
    this.splits,
    this.date,
    this.categoryId,
    this.cardId,
  });

  const InstallmentConversionExpenseRequest.create({
    required this.originalExpenseId,
    required this.amount,
    this.info,
    this.currencyId,
    this.splits,
    this.date,
    this.categoryId,
    this.cardId,
  });

  factory InstallmentConversionExpenseRequest.fromJson(Map<String, dynamic> json) {
    return InstallmentConversionExpenseRequest(
      originalExpenseId: json['expenseId'] as int,
      amount: json['amount'] as double,
      info: json['info'] as String?,
      currencyId: json['currencyId'] as int?,
      date: json['date'] != null ? DateTime.parse(json['date'] as String) : null,
      categoryId: json['categoryId'] as int?,
      cardId: json['cardId'] as int?,
    );
  }

  Map<String, dynamic> toJson() {
    return <String, dynamic>{
      'amount': amount,
      'info': info,
      'currencyId': currencyId,
      'date': date?.toIso8601String(),
      'categoryId': categoryId,
      'cardId': cardId,
    };
  }
}