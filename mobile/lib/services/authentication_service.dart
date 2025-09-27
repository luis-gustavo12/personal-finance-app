

import 'dart:convert';

import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:http/http.dart' as http;

class AuthenticationService {

  final _storage = FlutterSecureStorage();


  Future<bool> login (String email, String password) async {

    try {
      final response = await http.post(
        Uri.parse('http://10.0.2.2:8080/api/login'),
        headers: {
          'Content-Type': 'application/json; charset=UTF-8',
        },
        body: jsonEncode({
          'email': email,
          'password': password
        }),
      );

      if (response.statusCode == 200) {
        print('Login successfull!');
        Map<String, dynamic> responseBody = jsonDecode(response.body);
        final String? token = responseBody['token'];
        await _storage.write(key: 'auth_token', value: token);
        return true;
      } else {
        return false;
      }

    } catch (e) {
      print(e);
      return false;
    }

  }

  Future<String?> getToken() async {
    String? token = await _storage.read(key: 'auth_token');
    return token;
  }

}