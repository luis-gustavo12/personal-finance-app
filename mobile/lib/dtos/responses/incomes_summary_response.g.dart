// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'incomes_summary_response.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

IncomesSummaryResponse _$IncomesSummaryResponseFromJson(
  Map<String, dynamic> json,
) => IncomesSummaryResponse(
  currency: json['currency'] as String,
  amount: (json['amount'] as num).toDouble(),
  paymentForm: json['paymentForm'] as String,
  date: json['date'] as String,
  extraInfo: json['extraInfo'] as String,
  id: (json['id'] as num).toInt(),
);

Map<String, dynamic> _$IncomesSummaryResponseToJson(
  IncomesSummaryResponse instance,
) => <String, dynamic>{
  'currency': instance.currency,
  'amount': instance.amount,
  'paymentForm': instance.paymentForm,
  'date': instance.date,
  'extraInfo': instance.extraInfo,
  'id': instance.id,
};
