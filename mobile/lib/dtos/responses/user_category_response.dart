
class UserCategoryResponse {

  final int id;
  final String categoryName;

  UserCategoryResponse({required this.id, required this.categoryName});

  factory UserCategoryResponse.fromJson(Map<String, dynamic> json) {
    return UserCategoryResponse(
      id: json['id'] as int,
      categoryName:  json['categoryName'] as String
    );
  }

}