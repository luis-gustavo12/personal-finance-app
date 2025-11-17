
import 'package:logger/logger.dart';
import 'package:mobile/communication_handler.dart';
import 'package:mobile/dtos/requests/expense_creation_request.dart';
import 'package:mobile/dtos/requests/installment_conversion_expense_request.dart';
import 'package:mobile/dtos/requests/installment_expense_creation_request.dart';
import 'package:mobile/dtos/requests/simple_expense_conversion.dart';
import 'package:mobile/dtos/requests/update_installment_request.dart';
import 'package:mobile/dtos/requests/update_simple_expense_request.dart';
import 'package:mobile/dtos/responses/api_response.dart';
import 'package:mobile/dtos/responses/expenses_response.dart';

final logger = Logger();

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
      logger.i("Error: ${e.toString()}");
      return null;
    }
  }

  Future<ApiResponse?> sendSimpleExpense(ExpenseCreationRequest request) async {

    try {
      return await CommunicationHandler.sendData(data: request, route: "/api/expenses/simple-expense", method: Methods.post);
    } catch (e) {
      logger.i("Error: ${e.toString()}");
      return null;
    }
  }

  Future<ApiResponse?> sendInstallmentExpense(InstallmentExpenseCreationRequest request) async {

    try {
      return await CommunicationHandler.sendData(data: request, route: "/api/expenses/installment-expense", method: Methods.post);
    } catch (e) {
      logger.i("Error: ${e.toString()}");
      return null;
    }


  }

  Future<ApiResponse?> convertSimpleExpenseIntoInstallment(InstallmentConversionExpenseRequest request) async {
    
    try {
      return await CommunicationHandler.sendData(data: request, route: "/api/${request.expenseId}/convert-to-installment", method: Methods.post);
    } catch (e) {
      logger.i("Error: ${e.toString()}");

    }
    return null;
  }

  Future<ApiResponse?> convertInstallmentIntoSimpleExpense() async {
    return null;
  }

  Future<ApiResponse?> updateInstallment(int installmentId, UpdateInstallmentRequest request) async {


    try {
      return await CommunicationHandler.sendData(data: request, route: "/api/installments/$installmentId", method: Methods.put);
    } catch (e) {
      logger.i("Error: ${e.toString()}");
      return null;
    }

  }

  Future<ApiResponse?> convertInstallmentToSimple(int installmentId, SimpleExpenseConversion request) async {
    
    logger.i("Removing installment with id $installmentId}");
    
    try {
      return await CommunicationHandler.sendData(data: request, route: "/api/expenses/$installmentId/convert-simple-expense", method: Methods.post);
    } catch (e) {
      logger.i("Error: ${e.toString()}");
      return null;
    }
    
  }

  Future<ApiResponse?> convertSimpleExpenseToInstallment(InstallmentConversionExpenseRequest request) async {

    try {
      return await CommunicationHandler.sendData(data: request, route: "/api/expenses/convert-to-installment", method: Methods.post);
    } catch (e) {
      logger.i("Error: ${e.toString()}");
    }
    
    return null;
  }

  Future<ApiResponse?> deleteExpense(int id) async{
    try {
      return await CommunicationHandler.sendData(data: null, route: "/api/expenses/$id", method: Methods.delete);
    } catch (e) {
      logger.i("Error: ${e.toString()}");
      return null;
    }
  }

  Future<ApiResponse?> deleteInstallmentExpense(int id) async{
    try {
      return await CommunicationHandler.sendData(data: null, route: "/api/installments/$id", method: Methods.delete);
    } catch (e)  {
      logger.i("Error: ${e.toString()}");
      return null;
    }
  }

  Future<ApiResponse?> updateSimpleExpense(UpdateSimpleExpenseRequest request, int expenseId) async {
    
    try {
      return await CommunicationHandler.sendData(data: request, route: "/api/expenses/$expenseId", method: Methods.put);
    } catch (e) {
      logger.i("Error: ${e.toString()}");
      return null;
    }
    
  }




}