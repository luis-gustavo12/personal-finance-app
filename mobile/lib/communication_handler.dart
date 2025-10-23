import 'dart:convert';

import 'package:flutter_dotenv/flutter_dotenv.dart';
import 'package:http/http.dart' as http;
import 'package:mobile/dtos/responses/api_response.dart';
import 'package:mobile/services/authentication_service.dart';

enum Methods{
  get, post, put, patch, delete
}

class CommunicationHandler {

  static Map<String, String> getHeaders(String token) {
    return {
      'Content-Type': 'application/json; charset=UTF-8',
      'Authorization': 'Bearer $token',
    };
  }

  static Future<T?> fetchData<T>(String route) async {
    final AuthenticationService authenticationService = AuthenticationService();
    String? token = await authenticationService.getToken();

    if (token == null) return null;

    var headers = getHeaders(token);

    try {
      var authority = dotenv.env['BASE_URL'] ?? "http://localhost:8080";
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
  static Future<ApiResponse?> sendData({required dynamic data, required String route, required Methods method}) async{
    final AuthenticationService authenticationService = AuthenticationService();
    String? token = await authenticationService.getToken();

    if (token == null) return null;

    try {
      late final response;
      var authority = dotenv.env['BASE_URL'] ?? "http://localhost:8080";
      var uri = Uri.http(authority, route);
      var headers = getHeaders(token);
      var encodedData = jsonEncode(data);
      if (method == Methods.patch) {
        response = await http.patch(uri, headers: headers, body: encodedData);
      } else if (method == Methods.post) {
        response = await http.post(uri, headers: headers, body: encodedData);
      } else if (method == Methods.get) {
        response = await http.get(uri, headers: headers);
      }

      if (response.statusCode != 200) {
        print("Error received from server: ${response.statusCode}");
        print("Body: ${response.body}");
      }

      return ApiResponse(response.statusCode, response.body);


      return jsonDecode(response.body);

    } catch (e) {
      print("Error: ${e.toString()}");
      return null;
    }

  }
}
