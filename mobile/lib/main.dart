import 'package:flutter/material.dart';
import 'package:flutter_dotenv/flutter_dotenv.dart';
import 'package:mobile/screens/cards_page.dart';
import 'package:mobile/screens/expenses_page.dart';
import 'package:mobile/screens/incomes_page.dart';
import 'screens/login_page.dart';
import 'screens/home_page.dart';

Future<void> main() async {
  const environment = String.fromEnvironment('ENV', defaultValue: 'dev');
  await dotenv.load(fileName: ".env.$environment");
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
        '/home': (context) => const HomePage(),
        '/incomes': (context) => const IncomesPage(),
        '/expenses': (context) => const ExpensesPage(),
        '/cards': (context) => const CardsPage(),
      },
      initialRoute: '/',
    );
  }
}
