
import '../../models/currency.dart';
import '../../models/payment_methods.dart';

class IncomeFetchedDataRequest {
  final List<Currency> currencies;
  final List<PaymentMethods> paymentMethods;

  IncomeFetchedDataRequest({required this.currencies, required this.paymentMethods});
}