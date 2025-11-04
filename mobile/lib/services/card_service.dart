
import 'package:mobile/communication_handler.dart';
import 'package:mobile/dtos/responses/card_response.dart';

class CardService {
  
  
  Future<List<CardResponse>?> getUserCards() async {
    
    final List<dynamic>? response = await CommunicationHandler.fetchData("/api/cards");

    if (response == null) return null;

    return response.map((response) => CardResponse.fromJson(response)).toList();
    
  }

  String formatCard(CardResponse card) {
    return "${card.brand} ended in ${card.lastDigits} - ${card.cardName}";
  }
  
}