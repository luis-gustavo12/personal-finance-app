
import 'package:mobile/models/payment_methods.dart';
import 'package:mobile/communication_handler.dart';

class PaymentMethodsService {
  
  Future<List<PaymentMethod>> fetchPaymentMethods() async {
    
    try {
      final List<dynamic>? response = await CommunicationHandler.fetchData("/api/payment-methods");

      if (response != null) {
        return response.map((item) => PaymentMethod.fromJson(item)).toList();
      }

      return [];

    } catch (e) {
      print("Error: ${e.toString()}");
      return [];
    }
    
  }
}