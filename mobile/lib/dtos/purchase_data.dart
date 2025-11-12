
import 'package:mobile/dtos/responses/card_response.dart';
import 'package:mobile/dtos/responses/user_category_response.dart';
import 'package:mobile/models/payment_methods.dart';

import '../models/currency.dart';

/// Required for any kind of purchase
class PurchaseData {
  final List<Currency>? currencies;
  final List<UserCategoryResponse>? categories;
  final List<PaymentMethod> paymentMethods;
  final List<CardResponse>? cards;

  PurchaseData({required this.currencies, required this.categories, required this.paymentMethods, this.cards});
}