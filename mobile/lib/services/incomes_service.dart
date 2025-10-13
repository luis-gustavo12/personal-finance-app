import 'package:mobile/dtos/requests/income_update_request.dart';
import 'package:mobile/dtos/responses/incomes_summary_response.dart';
import 'package:mobile/communication_handler.dart';

class IncomesService {
  Future<List<IncomesSummaryResponse>?> getIncomesData() async {
    try {
      final List<dynamic>? response = await CommunicationHandler.fetchData(
        "/api/incomes",
      );
      if (response == null) return null;

      return IncomesSummaryResponse.fromJsonList(response);
    } catch (e) {
      return null;
    }
  }
  Future<bool> updateIncome ({required IncomeUpdateRequest request, required int incomeId}) async {
    final response = await CommunicationHandler.sendData(data: request, route: "/api/incomes/edit/$incomeId", method: Methods.patch);
    if (response == null) {
      return false;
    }
    if (response.status != 200) {
      return false;
    }
    return true;

  }
}
