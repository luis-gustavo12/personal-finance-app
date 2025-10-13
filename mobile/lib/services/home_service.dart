
import 'dart:convert';

import 'package:flutter_dotenv/flutter_dotenv.dart';
import 'package:http/http.dart' as http;
import 'package:mobile/dtos/responses/dashboard_response.dart';
import 'package:mobile/services/authentication_service.dart';

class HomeService {

  final AuthenticationService _authenticationService = AuthenticationService();

  Future<DashboardResponse?> getDataFromServer () async {

    try {

      String? token = await _authenticationService.getToken();

      final String baseUrl = dotenv.env['BASE_URL'] ?? 'http://localhost:8080';

      if (token == null) {
        print('Could not read token!!');
        return null;
      }

      var response = await http.get(
        Uri.parse('http://$baseUrl/api/dashboard'),
        headers: {
          'Content-Type': 'application/json; charset=UTF-8',
          'Authorization': 'Bearer $token',
        },
      );

      if (response.statusCode != 200) {
        try {
          var errorBody = jsonDecode(response.body);
          String errorMessage = errorBody['error'] ?? 'Unknown server error.';
          print('HTTP Error ${response.statusCode}: $errorMessage');
          return null;
        } catch (e) {
          print('HTTP Error ${response.statusCode}. Could not decode error body: $e');
          return null;
        }
      }

      final Map<String, dynamic> data = jsonDecode(response.body);
      return DashboardResponse.fromJson(data);

    } catch (e) {
      print('Exception occurred: $e');
      return null;
    }
    

  }


}