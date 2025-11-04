
import 'dart:ffi';

class PaymentMethod {
  final int id;
  final String description;

  const PaymentMethod({required this.id, required this.description});

  factory PaymentMethod.fromJson(Map<String, dynamic> json) {
    return PaymentMethod(id: json['id'], description: json['description']);
  }

}