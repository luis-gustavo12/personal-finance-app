// Converts one installment to a simple request
class SimpleExpenseConversion {
  final double amount;
  final int paymentMethodId;
  final int categoryId;
  final int currencyId;
  final DateTime date;
  final String? description;
  final int? cardId;

  SimpleExpenseConversion({
    required this.amount,
    required this.paymentMethodId,
    required this.categoryId,
    required this.currencyId,
    required this.date,
    this.description,
    this.cardId,
  });

  factory SimpleExpenseConversion.fromJson(Map<String, dynamic> json) {
    return SimpleExpenseConversion(
      amount: (json['amount'] as num).toDouble(),
      paymentMethodId: json['paymentMethodId'] as int,
      categoryId: json['categoryId'] as int,
      currencyId: json['currencyId'] as int,
      date: DateTime.parse(json['date'] as String),
      description: json['description'] as String?,
      cardId: json['cardId'] as int?,
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'amount': amount,
      'paymentMethodId': paymentMethodId,
      'categoryId': categoryId,
      'currencyId': currencyId,
      'date': date.toIso8601String(),
      'description': description,
      'cardId': cardId,
    };
  }
}