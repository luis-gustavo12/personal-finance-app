
import 'dart:ffi';

class CardResponse {
  final int id;
  final String cardName;
  final int expirationMonth;
  final int expirationYear;
  final String lastDigits;
  final String brand;
  final String cardType;

  CardResponse({required this.id, required this.cardName, required this.expirationMonth, required this.expirationYear, required this.brand, required this.cardType, required this.lastDigits});

  factory CardResponse.fromJson(Map<String, dynamic> json) {
    return CardResponse(id: json['id'] as int,
        cardName: json['cardName'] as String,
        expirationMonth: json['expirationMonth'] as int,
        expirationYear: json['expirationYear'] as int,
        brand: json['brand'] as String,
        cardType: json['cardType'] as String,
        lastDigits: json['lastDigits'] as String,
    );
  }

}