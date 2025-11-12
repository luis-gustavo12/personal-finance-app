
class CardDataResponse {
  final int id;
  final String cardName;
  final int expirationMonth;
  final int expirationYear;
  final String lastDigits;
  final String brand;
  final String cardType;

  CardDataResponse({
    required this.id,
    required this.cardName,
    required this.expirationMonth,
    required this.expirationYear,
    required this.lastDigits,
    required this.brand,
    required this.cardType,
  });

  factory CardDataResponse.fromJson(Map<String, dynamic> json) {
    return CardDataResponse(
      id: json['id'] as int,
      cardName: json['cardName'] as String,
      expirationMonth: json['expirationMonth'] as int,
      expirationYear: json['expirationYear'] as int,
      lastDigits: json['lastDigits'] as String,
      brand: json['brand'] as String,
      cardType: json['cardType'] as String,
    );
  }

  Map<String, dynamic> toJson() {
    return <String, dynamic>{
      'id': id,
      'cardName': cardName,
      'expirationMonth': expirationMonth,
      'expirationYear': expirationYear,
      'lastDigits': lastDigits,
      'brand': brand,
      'cardType': cardType,
    };
  }
}