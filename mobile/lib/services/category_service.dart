
import 'package:mobile/communication_handler.dart';
import 'package:mobile/dtos/responses/user_category_response.dart';

class CategoryService {

  Future<List<UserCategoryResponse>?> getAllUserCategories() async{

    final response = await CommunicationHandler.fetchData("/api/category");

    if (response != null) {
      var s = response.map<UserCategoryResponse>(
              (item) => UserCategoryResponse.fromJson(item as Map<String, dynamic>)).toList();
      print(s);
      return response.map<UserCategoryResponse>(
              (item) => UserCategoryResponse.fromJson(item as Map<String, dynamic>)).toList();
    }

    return null;
  }


}