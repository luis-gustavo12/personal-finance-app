

class Summary {

  final String currency;
  final double amount;


  Summary({
    required this.currency, required this.amount
  });

  factory Summary.fromJson(Map<String, dynamic> json) {
    return Summary(currency: json['currency'] as String,
          amount: (json['amount'] as num).toDouble(),);
  }


}

class DashboardResponse {

  final Summary expenses;
  final Summary incomes;

  DashboardResponse({
    required this.expenses, required this.incomes
  });

  factory DashboardResponse.fromJson(Map<String, dynamic> json) {
    return DashboardResponse(
      expenses: Summary.fromJson(json['expensesSummary'] as Map<String, dynamic>),
      incomes: Summary.fromJson(json['incomesSummary'] as Map<String, dynamic>)
    );
  }


}