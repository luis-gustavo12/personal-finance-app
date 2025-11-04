
class ExpenseResponse {
  final int id;
  final String currency;
  final String paymentMethod;
  final double amount;
  final String? info;
  final DateTime date;
  final String category;

  ExpenseResponse({required this.id, required this.currency, required this.paymentMethod, required this.amount, this.info, required this.date, required this.category});

  factory ExpenseResponse.fromJson(Map<String, dynamic> json) {
    return ExpenseResponse(
      id: json['id'] as int,
      currency: json['currency'] as String,
      paymentMethod: json['paymentMethod'] as String,
      amount: json['amount'] as double,
      info: json['info'] as String?, // Note: Handles nullable 'info'
      date: DateTime.parse(json['date'] as String),
      category: json['category'] as String,
    );
  }

  Map<String, dynamic> toJson() {
    return <String, dynamic>{
      'id': id,
      'currency': currency,
      'paymentMethod': paymentMethod,
      'amount': amount,
      'info': info,

      'date': date.toIso8601String(),
      'category': category,
    };
  }
}