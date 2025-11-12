import 'package:mobile/dtos/responses/card_data_response.dart';
import 'package:mobile/dtos/responses/installment_expense_response.dart';

class ExpenseResponse {
  final int id;
  final String currency;
  final String paymentMethod;
  final double amount;
  final String? info;
  final DateTime date;
  final String category;
  final InstallmentExpenseResponse? installment;
  final CardDataResponse? cardDataResponse;

  ExpenseResponse({
    required this.id,
    required this.currency,
    required this.paymentMethod,
    required this.amount,
    this.info,
    required this.date,
    required this.category,
    this.installment,
    this.cardDataResponse
  });

  factory ExpenseResponse.fromJson(Map<String, dynamic> json) {

    final double parsedAmount = (json['amount'] is int)
        ? (json['amount'] as int).toDouble()
        : json['amount'] as double;
    final installmentJson = json['installment'];
    final InstallmentExpenseResponse? parsedInstallment = (installmentJson != null)
        ? InstallmentExpenseResponse.fromJson(installmentJson as Map<String, dynamic>)
        : null;

    final cardDataJson = json['cardData'];
    final CardDataResponse? cardData = (cardDataJson != null)
        ? CardDataResponse.fromJson(cardDataJson as Map<String, dynamic>)
        : null;

    return ExpenseResponse(
      id: json['id'] as int,
      currency: json['currency'] as String,
      paymentMethod: json['paymentMethod'] as String,
      amount: json['amount'] as double,
      info: json['info'] as String?,
      date: DateTime.parse(json['date'] as String),
      category: json['category'] as String,
      installment: parsedInstallment,
      cardDataResponse: cardData
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
      'installment': installment?.toJson(),
      'cardData': cardDataResponse?.toJson()
    };
  }
}
