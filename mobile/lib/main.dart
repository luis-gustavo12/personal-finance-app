import 'package:flutter/material.dart';
import 'screens/login_page.dart';
import 'screens/home_page.dart';

void main() {
  runApp(const MainFinanceApp());
}

class MainFinanceApp extends StatelessWidget {

  const MainFinanceApp({super.key});

  @override
  Widget build(BuildContext context) {

    return MaterialApp(
      title: "Finance App",
      routes: {
        '/': (context) => const LoginPage(),
        '/home': (context) => const HomePage()
      },
      initialRoute: '/',
    );

  }

}
