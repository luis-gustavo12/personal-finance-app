
class IncomeUpdateRequest {
  final int currencyId;
  final double incomeAmount;
  final int paymentMethodId;
  final DateTime date;
  final String incomeDescription;

  IncomeUpdateRequest({required this.currencyId, required this.incomeAmount, required this.paymentMethodId, required this.date, required this.incomeDescription});

  Map<String, dynamic> toJson() {
    return {
      'currencyId': currencyId,
      'incomeAmount': incomeAmount,
      'paymentMethodId': paymentMethodId,
      // DateTime must be converted to an ISO 8601 String for JSON
      'date': date.toIso8601String(),
      'incomeDescription': incomeDescription,
    };
  }

}