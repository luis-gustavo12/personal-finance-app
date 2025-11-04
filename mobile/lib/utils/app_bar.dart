

import 'package:flutter/material.dart';

class AppBarUtil {

  static AppBar appBar(String title, Color backgroundColor, Color iconColor) {
    return AppBar(
      backgroundColor: backgroundColor,
      leading: Builder(
        builder: (context) => IconButton(
          onPressed: () {
            Navigator.pushNamed(context, '/home');
          },
          icon: Icon(Icons.arrow_back, color: iconColor, size: 32),
        ),
      ),
      title: Text(title),
    );
  }

}