
import 'package:mobile/communication_handler.dart';
import 'package:mobile/dtos/requests/expense_creation_request.dart';
import 'package:mobile/dtos/requests/installment_expense_creation_request.dart';
import 'package:mobile/dtos/responses/api_response.dart';
import 'package:mobile/dtos/responses/expenses_response.dart';

class ExpensesService {
  Future<List<ExpenseResponse>?> getUserExpenses() async {

    try {
     final List<dynamic>? response = await CommunicationHandler.fetchData("/api/expenses");
     if (response == null) return null;
     return response
         .cast<Map<String, dynamic>>()
         .map((jsonMap) => ExpenseResponse.fromJson(jsonMap))
         .toList();
    } catch (e) {
      print("Error: ${e.toString()}");
      return null;
    }
  }

  Future<ApiResponse?> sendSimpleExpense(ExpenseCreationRequest request) async {

    try {
      return await CommunicationHandler.sendData(data: request, route: "/api/expenses/simple-expense", method: Methods.post);
    } catch (e) {
      print("Error: ${e.toString()}");
      return null;
    }
  }

  Future<ApiResponse?> sendInstallmentExpense(InstallmentExpenseCreationRequest request) async {

    try {
      return await CommunicationHandler.sendData(data: request, route: "/api/expenses/installment-expense", method: Methods.post);
    } catch (e) {
      print("Error: ${e.toString()}");
      return null;
    }


  }


}