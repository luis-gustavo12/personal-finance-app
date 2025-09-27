// lib/screens/home_page.dart
import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'package:mobile/dtos/responses/dashboard_response.dart';
import 'package:mobile/services/home_service.dart';

class HomePage extends StatefulWidget {
  const HomePage({super.key});

  @override
  State<StatefulWidget> createState() => _HomeState();
}

class _HomeState extends State<HomePage> {
  DashboardResponse? _dashboardResponse;
  bool _isLoading = true;
  final HomeService _homeService = HomeService();

  @override
  void initState() {
    super.initState();
    _loadData();
  }

  Future<void> _loadData() async {
    try {
      final response = await _homeService.getDataFromServer();

      setState(() {
        _isLoading = false;
        _dashboardResponse = response;
      });
    } catch (e) {
      print('Error occurred: $e');
      _isLoading = false;
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        backgroundColor: Colors.blueGrey,
        title: const Text('Dashboard'),
        actions: [
          IconButton(
            icon: const Icon(Icons.logout),
            onPressed: () {
              Navigator.pushReplacementNamed(context, '/');
            },
          ),
        ],
      ),
      body: Center(
        child: _isLoading ? const CircularProgressIndicator()
            : _dashboardResponse == null ? const Text("Server error, please, try again later!!")
            : _buildDashboardScreen(),
      )
    );
  }

  Widget _buildDashboardScreen() {
    final incomes = _dashboardResponse!.incomes;
    final expenses = _dashboardResponse!.expenses;

    return Column(
      mainAxisAlignment: MainAxisAlignment.start,
      crossAxisAlignment: CrossAxisAlignment.center,
      children: <Widget>[
        SizedBox(height: 20,),
        Container(
          padding: const EdgeInsets.all(16),
          decoration: BoxDecoration(
            color: Colors.green[100],
            borderRadius: BorderRadius.circular(8),
          ),
          width: 350,
          child: Column(
            children: [
              Text('Incomes', style: TextStyle(
                fontSize: 20, fontWeight: FontWeight.bold
              ),),
              SizedBox(height: 8,),
              Text('${expenses.currency} ${expenses.amount.toStringAsFixed(2)}', style: const TextStyle(fontSize: 24, color: Colors.red),)
            ],
          ),
        )
      ],

    );

  }

}
