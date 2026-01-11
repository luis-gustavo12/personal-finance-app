// This file will contain every modal for the Card entity
// Currently, only edit and add card

// EDIT MODAL

import 'package:flutter/material.dart';
import 'package:mobile/dtos/responses/card_response.dart';
import 'package:mobile/dtos/responses/token_response.dart';
import 'package:mobile/services/token_service.dart';

void showEditCardModal(BuildContext context, CardResponse card) async {
  showModalBottomSheet(
    context: context,
    builder: (ctx) => EditCardModal(card: card),
  );
}

class EditCardModal extends StatefulWidget {
  final CardResponse card;

  const EditCardModal({super.key, required this.card});

  @override
  State<StatefulWidget> createState() => _EditCardModalState();
}

class _EditCardModalState extends State<EditCardModal> {
  final TokenService _tokenService = TokenService();
  String? _token;
  bool _isLoading = true;
  TokenResponse? tokenResponse;
  bool _failure = false;
  final _formKey = GlobalKey<FormState>();

  @override
  void initState() {
    super.initState();
    _loadToken();
  }

  Future<void> _loadToken() async {
    tokenResponse = await _tokenService.getToken();

    if (tokenResponse == null) {
      _failure = false;
      return;
    }

    if (mounted) {
      setState(() {
        _token = tokenResponse!.token;
        _isLoading = false;
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    if (_isLoading) {
      return Center(child: CircularProgressIndicator());
    }

    if (_failure) {
      return SizedBox(
        height: 250,
        child: Center(
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              const Icon(Icons.error_outline, color: Colors.red, size: 48),
              const SizedBox(height: 16),
              const Text("Sorry, an error occurred!!"),
              const SizedBox(height: 24),
              ElevatedButton(
                onPressed: () {
                  Navigator.pushNamed(context, "/");
                },
                child: const Text("Go to Home Page"),
              ),
            ],
          ),
        ),
      );
    }

    return Container(
      padding: EdgeInsets.only(
        top: 20,
        left: 20,
        right: 20,
        bottom: MediaQuery.of(context).viewInsets.bottom + 20.0,
      ),
      width: double.infinity,
      child: Form(
        key: _formKey,
        child: Column(
          mainAxisAlignment: MainAxisAlignment.start,
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text("Edite seu cartão"),
            SizedBox(height: 16,),

          ],
        ),
      ),
    );
  }
}

// ADD MODAL

void showCreateCardModal(BuildContext context) async {
  showModalBottomSheet(context: context, builder: (ctx) => AddCardModal());
}

class AddCardModal extends StatefulWidget {
  const AddCardModal({super.key});

  @override
  State<StatefulWidget> createState() => _AddCardModalState();
}

class _AddCardModalState extends State<AddCardModal> {
  @override
  Widget build(BuildContext context) {
    return Text("Hello World!!");
  }
}
