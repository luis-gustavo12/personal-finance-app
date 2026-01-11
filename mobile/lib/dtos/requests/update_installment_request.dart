class UpdateInstallmentRequest {
  final double amount;
  final int splits;
  final int cardId;
  final int categoryId;
  final int currencyId;
  final DateTime date;
  final String? description;

  const UpdateInstallmentRequest({
    required this.amount,
    required this.splits,
    required this.cardId,
    required this.categoryId,
    required this.currencyId,
    required this.date,
    this.description,
  });

  factory UpdateInstallmentRequest.fromJson(Map<String, dynamic> json) {
    return UpdateInstallmentRequest(
      amount: (json['amount'] as num).toDouble(),
      splits: json['splits'] as int,
      cardId: json['cardId'] as int,
      categoryId: json['categoryId'] as int,
      currencyId: json['currencyId'] as int,
      date: DateTime.parse(json['date'] as String),
      description: json['info'] as String?,
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'amount': amount,
      'splits': splits,
      'cardId': cardId,
      'categoryId': categoryId,
      'currencyId': currencyId,
      'date': date.toIso8601String(),
      'description': description,
    };
  }
}