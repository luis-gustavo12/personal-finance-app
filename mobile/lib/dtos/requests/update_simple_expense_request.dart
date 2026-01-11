class UpdateSimpleExpenseRequest {
  final double? amount;
  final int? splits;
  final String? extraInfo;
  final int? paymentMethodId;
  final int? cardId;
  final DateTime? date;
  final int? currencyId;
  final int? categoryId;

  const UpdateSimpleExpenseRequest({
    this.amount,
    this.splits,
    this.extraInfo,
    this.paymentMethodId,
    this.cardId,
    this.date,
    this.currencyId,
    this.categoryId
  });

  factory UpdateSimpleExpenseRequest.fromJson(Map<String, dynamic> json) {
    return UpdateSimpleExpenseRequest(
      amount: json['amount'] as double?,
      splits: json['splits'] as int?,
      extraInfo: json['extraInfo'] as String?,
      paymentMethodId: json['paymentMethodId'] as int?,
      cardId: json['cardId'] as int?,
      date: json['date'] != null ? DateTime.parse(json['date'] as String) : null,
      currencyId: json['currencyId'] as int?,
      categoryId: json['categoryId'] as int?
    );
  }

  Map<String, dynamic> toJson() {
    return <String, dynamic>{
      'amount': amount,
      'splits': splits,
      'extraInfo': extraInfo,
      'paymentMethodId': paymentMethodId,
      'cardId': cardId,
      'date': date?.toIso8601String(),
      'currencyId': currencyId,
      'categoryId': categoryId
    };
  }
}