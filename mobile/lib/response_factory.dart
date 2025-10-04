import 'dart:convert';

import 'package:http/http.dart' as http;
import 'package:mobile/services/authentication_service.dart';

class ResponseFactory {
  static Future<T?> fetchData<T>(String route) async {
    AuthenticationService authenticationService = AuthenticationService();

    String? token = await authenticationService.getToken();

    if (token == null) return null;

    Map<String, String> headers = {
      'Content-Type': 'application/json; charset=UTF-8',
      'Authorization': 'Bearer $token',
    };

    try {
      var authority = '10.0.2.2:8080';
      var uri = Uri.http(authority, route);
      print("Final Uri: $uri");
      var response = await http.get(uri, headers: headers);

      if (response.statusCode != 200) {
        print("Error received from server: ${response.statusCode}");
        print("Body: ${response.body}");
        return null;
      }

      return jsonDecode(response.body);
    } catch (e) {
      return null;
    }
  }
}
