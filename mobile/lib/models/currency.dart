
class Currency {
  final int id;
  final String currencyName;
  final String currencyFlag;
  final String currencySymbol;
  final int decimalPlaces;

  const Currency({
    required this.id,
    required this.currencyName,
    required this.currencyFlag,
    required this.currencySymbol,
    required this.decimalPlaces
  });

  factory Currency.fromJson(Map<String, dynamic> json) {
    return Currency(
      id: json['id'],
      currencyName: json['currencyName'],
      currencyFlag: json['currencyFlag'],
      currencySymbol: json['currencySymbol'],
      decimalPlaces: json['decimalPlaces'] as int
    );
  }


}