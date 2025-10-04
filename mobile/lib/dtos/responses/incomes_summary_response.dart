import 'package:json_annotation/json_annotation.dart';
part 'incomes_summary_response.g.dart';

@JsonSerializable()
class IncomesSummaryResponse {
  final String currency;
  final double amount;
  final String paymentForm;
  final String date;
  final String extraInfo;
  final int id;

  IncomesSummaryResponse({
    required this.currency,
    required this.amount,
    required this.paymentForm,
    required this.date,
    required this.extraInfo,
    required this.id,
  });

  factory IncomesSummaryResponse.fromJson(Map<String, dynamic> json) =>
      _$IncomesSummaryResponseFromJson(json);

  static List<IncomesSummaryResponse> fromJsonList(List<dynamic> jsonList) {
    return jsonList
        .map(
          (json) =>
              IncomesSummaryResponse.fromJson(json as Map<String, dynamic>),
        )
        .toList();
  }

  Map<String, dynamic> toJson() => _$IncomesSummaryResponseToJson(this);
}
