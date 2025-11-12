class InstallmentExpenseResponse {
  final int id;
  final double amount;
  final int splits;

  InstallmentExpenseResponse({
    required this.id,
    required this.amount,
    required this.splits,
  });

  factory InstallmentExpenseResponse.fromJson(Map<String, dynamic> json) {
    return InstallmentExpenseResponse(
      id: json['id'] as int,
      amount: json['amount'] as double,
      splits: json['splits'] as int,
    );
  }

  Map<String, dynamic> toJson() {
    return <String, dynamic>{
      'id': id,
      'amount': amount,
      'splits': splits,
    };
  }

}
