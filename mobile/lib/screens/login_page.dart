
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:mobile/services/authentication_service.dart';

class LoginPage extends StatefulWidget {
  const LoginPage({super.key});
  @override
  State<StatefulWidget> createState() => _LoginState();

}

class _LoginState extends State<LoginPage> {

  final _formKey = GlobalKey<FormState>();
  final TextEditingController _emailController = TextEditingController();
  final TextEditingController _pwdController = TextEditingController();
  final AuthenticationService _authenticationService = AuthenticationService();

  bool _isLoading = false;

  Future<void> _login() async {

    if (_formKey.currentState!.validate()) {
      setState(() {
        _isLoading = false;
      });

      bool success = await _authenticationService.login(
        _emailController.text,
        _pwdController.text,
      );

      if (!mounted) return;

      setState(() {
        _isLoading = false;
      });
      
      if (success) {
        Navigator.pushReplacementNamed(context, '/home');
        return;
      }

      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Failed to login. Please check your credentials.')),
      );

    }

  }

  @override
  void dispose() {
    _emailController.dispose();
    _pwdController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Login'),
      ),
      body: Center(
        child: SingleChildScrollView(
            padding: const EdgeInsets.all(16.0),
            child: Form(
              key: _formKey,
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                children: <Widget> [

                  Text('Conecte-se à sua conta!', style: TextStyle(
                      fontSize: 25
                  ),),

                  const SizedBox(height: 18,),

                  Image.asset(
                    'assets/images/android-chrome-512x512.png',
                    width: 150,
                    height: 150,
                  ),

                  const SizedBox(height: 20),
                  TextFormField(
                    controller: _emailController,
                    keyboardType: TextInputType.emailAddress,
                    decoration: const InputDecoration(
                      labelText: 'Email',
                      border: OutlineInputBorder(),
                      prefixIcon: Icon(Icons.email),
                    ),
                    validator: (value) {
                      if (value == null || value.isEmpty){
                        return 'Please, enter your email';
                      }
                      return null;
                    },

                  ),

                  const SizedBox(height: 16,),

                  TextFormField(
                      controller: _pwdController,
                      obscureText: true,
                      decoration: const InputDecoration(
                          labelText: 'Password',
                          border: OutlineInputBorder(),
                          prefixIcon: Icon(Icons.lock)
                      ),
                      validator: (value) {
                        if (value == null || value.isEmpty)
                          return 'Please enter your password';
                        return null;
                      }
                  ),

                  const SizedBox(height: 20,),

                  SizedBox(
                    width: double.infinity,
                    child: ElevatedButton(
                        onPressed: _login,
                        style: ElevatedButton.styleFrom(
                          padding: const EdgeInsets.symmetric(vertical: 16),
                        ),
                        child: const Text('Log in', style: TextStyle(fontSize: 18),)
                    ),
                  ),

                  const SizedBox(height: 16,),

                  TextButton(onPressed: (){}, child: const Text('Forgot Password?')),

                ],
              ),
            )
        ),
      ),
    );
  }


}