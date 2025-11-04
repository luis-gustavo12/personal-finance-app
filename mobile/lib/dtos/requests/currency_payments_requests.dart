
import '../../models/currency.dart';
import '../../models/payment_methods.dart';

class TransactionRequiredDataRequest {
  final List<Currency> currencies;
  final List<PaymentMethod> paymentMethods;

  TransactionRequiredDataRequest({required this.currencies, required this.paymentMethods});
}