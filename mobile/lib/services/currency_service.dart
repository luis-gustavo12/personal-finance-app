
import 'dart:convert';

import 'package:flutter_dotenv/flutter_dotenv.dart';
import 'package:mobile/models/currency.dart';
import 'package:mobile/communication_handler.dart';
import 'package:shared_preferences/shared_preferences.dart';

class CurrencyService {

  Future<List<Currency>> fetchCurrencies () async {

    try {
      final List<dynamic>? response = await CommunicationHandler.fetchData("/api/currency");

      if (response != null) {
        return response.map((item) => Currency.fromJson(item)).toList();
      }

      return [];

    } catch (e) {
      return [];
    }

  }

  // Future<List<Currency>>? getCurrencies() async{
  //
  //   final prefs = await SharedPreferences.getInstance();
  //   final cachedData = prefs.getString("currencies");
  //
  //   if (cachedData == null) {
  //     var list = fetchCurrencies();
  //     if (list == null) {
  //       return [];
  //     }
  //     prefs.setString("currencies", list.toString());
  //   }
  //
  //   List<dynamic> data = json.decode(cachedData);
  //
  //   return null;
  //
  // }

}

