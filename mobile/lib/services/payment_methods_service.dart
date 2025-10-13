
import 'package:mobile/models/payment_methods.dart';
import 'package:mobile/communication_handler.dart';

class PaymentMethodsService {
  
  Future<List<PaymentMethods>> fetchPaymentMethods() async {
    
    try {
      final List<dynamic>? response = await CommunicationHandler.fetchData("/api/payment-methods");

      if (response != null) {
        return response.map((item) => PaymentMethods.fromJson(item)).toList();
      }

      return [];

    } catch (e) {
      print("Error: ${e.toString()}");
      return [];
    }
    
  }
}