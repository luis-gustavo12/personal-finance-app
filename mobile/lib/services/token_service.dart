import 'package:logger/logger.dart';
import 'package:mobile/communication_handler.dart';
import 'package:mobile/dtos/responses/token_response.dart';

/// This service is designed to get the
/// Token from the CardGatewayProvider. Current, it is the stripe
/// token. It is ok for this token to be exposed, since the Web pages
/// Also do expose it.
///

final logger = Logger();

class TokenService {


  Future<TokenResponse?> getToken() async {

    try {
      final Map<String, dynamic>? response = await CommunicationHandler.fetchData<Map<String, dynamic>>("/api/token");
      if (response != null) {
        return TokenResponse.fromJson(response);
      }
    } catch (e) {
      logger.i("Error: $e");
    }

    return null;
  }


}