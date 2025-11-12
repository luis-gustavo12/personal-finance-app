import 'dart:async';
import 'dart:convert';

import 'package:flutter_dotenv/flutter_dotenv.dart';
import 'package:http/http.dart' as http;
import 'package:mobile/dtos/responses/api_response.dart';
import 'package:mobile/services/authentication_service.dart';
import 'package:logger/logger.dart';

enum Methods { get, post, put, patch, delete }

final logger = Logger();

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
      logger.i("Final Uri: $uri");
      var response = await http
          .get(uri, headers: headers)
          .timeout(const Duration(seconds: 15));

      if (response.statusCode != 200) {
        logger.i("Error received from server: ${response.statusCode}");
        logger.i("Body: ${response.body}");
        return null;
      }

      logger.i("Received: ${response.body}");

      return jsonDecode(response.body);
    } on TimeoutException catch(e) {
      logger.i("Request to $route timed out: ${e.message}");
      return null;
    }
    catch (e) {
      return null;
    }
  }

  static Future<ApiResponse?> sendData({
    required dynamic data,
    required String route,
    required Methods method,
  }) async {
    final AuthenticationService authenticationService = AuthenticationService();
    String? token = await authenticationService.getToken();

    if (token == null) return null;

    logger.i("Sending data to: $route");

    try {
      late final response;
      var authority = dotenv.env['BASE_URL'] ?? "http://localhost:8080";
      var uri = Uri.http(authority, route);
      var headers = getHeaders(token);
      var encodedData = jsonEncode(data);
      Future<http.Response> requestFuture;

      if (method == Methods.patch ) {
        requestFuture = http.patch(uri, headers: headers, body: encodedData);
      } else if (method == Methods.put) { // Handle PUT separately
        requestFuture = http.put(uri, headers: headers, body: encodedData);
      } else if (method == Methods.post) {
        requestFuture = http.post(uri, headers: headers, body: encodedData);
      } else if (method == Methods.get) {
        requestFuture = http.get(uri, headers: headers);
      } else {
        return null;
      }

      response = await requestFuture.timeout(const Duration(seconds: 15));

      if (response.statusCode != 200) {
        logger.i("Error received from server: ${response.statusCode}");
        logger.i("Body: ${response.body}");
      }

      logger.i("Received: ${response.body}");

      return ApiResponse(response.statusCode, response.body);
    } catch (e) {
      logger.i("Error: ${e.toString()}");
      return null;
    }
  }
}
