import 'package:mobile/dtos/responses/incomes_summary_response.dart';
import 'package:mobile/response_factory.dart';

class IncomesService {
  Future<List<IncomesSummaryResponse>?> getIncomesData() async {
    try {
      final List<dynamic>? response = await ResponseFactory.fetchData(
        "/api/incomes",
      );
      if (response == null) return null;

      return IncomesSummaryResponse.fromJsonList(response);
    } catch (e) {
      return null;
    }
  }
}
