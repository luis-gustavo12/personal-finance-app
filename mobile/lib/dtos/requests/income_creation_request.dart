
class IncomeCreationRequest {

  final int currencyId;
  final int paymentMethodId;
  final DateTime incomeDate;
  final double amount;
  final String description;

  IncomeCreationRequest(this.description, {required this.currencyId, required this.paymentMethodId, required this.incomeDate, required this.amount});

  Map<String, dynamic> toJson() {
    return {
      'amount': amount,
      'paymentMethodId': paymentMethodId,
      // DateTime must be converted to an ISO 8601 String for JSON
      'date': incomeDate.toIso8601String(),
      'currencyId' : currencyId,
      'description': description};
  }


}