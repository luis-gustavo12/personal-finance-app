import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:mobile/dtos/responses/card_response.dart';
import 'package:mobile/services/card_service.dart';
import 'package:mobile/theme/colors.dart';

import '../modals/cards_modal.dart';
import '../utils/app_bar.dart';

class CardsPage extends StatefulWidget {
  const CardsPage({super.key});
  @override
  State<StatefulWidget> createState() => CardsState();
}

class CardsState extends State<CardsPage> {
  final _cardsService = CardService();
  List<CardResponse>? _cards;
  bool _isLoadingCards = false;

  @override
  void initState() {
    super.initState();
    _getCards();
  }

  void _getCards() async {
    setState(() {
      _isLoadingCards = true;
    });
    _cards = await _cardsService.getUserCards();
    if (mounted) {
      setState(() {
        _isLoadingCards = false;
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBarUtil.appBar("Cartões", AppColors.lilac, AppColors.mainBlue),
      floatingActionButton: FloatingActionButton(
        onPressed: () {},
        child: Icon(Icons.add),
      ),
      body: _isLoadingCards
          ? Center(child: CircularProgressIndicator())
          : _cards == null
          ? Text("Could not connect to the server!!")
          : _cards!.isEmpty
          ? Text("You have no cards, please, assign a new one")
          : buildCardsColumn(),
    );
  }

  Widget buildCardsColumn() {
    return ListView(
      children: [
        SizedBox(height: 8),
        ..._cards!.map((card) {
          return Card(
            elevation: 8.0,
            shape: RoundedRectangleBorder(
              borderRadius: BorderRadius.circular(15.0),
            ),
            margin: EdgeInsets.all(16.0),
            child: Stack(
              children: [
                Container(
                  height: 220,
                  decoration: BoxDecoration(
                    borderRadius: BorderRadius.circular(15.0),
                    gradient: LinearGradient(
                      colors: [
                        AppColors.lilac,
                        AppColors.lilac.withOpacity(0.7),
                      ],
                      begin: Alignment.topLeft,
                      end: Alignment.bottomRight,
                    ),
                  ),
                  child: Padding(
                    padding: const EdgeInsets.all(20.0),
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: <Widget>[
                        Text(
                          card.cardType,
                          style: TextStyle(
                            color: Colors.black,
                            fontSize: 14.0,
                            fontWeight: FontWeight.bold,
                            letterSpacing: 3,
                          ),
                        ),
                        SizedBox(height: 5),
                        Text(
                          card.brand.toUpperCase(),
                          style: TextStyle(
                            color: Colors.black,
                            fontWeight: FontWeight.bold,
                            fontStyle: FontStyle.italic,
                          ),
                        ),
                        const Spacer(),
                        Text(
                          "**** **** ${card.lastDigits}",
                          style: const TextStyle(
                            color: Colors.black,
                            fontSize: 22.0,
                            letterSpacing: 3.0,
                            fontWeight: FontWeight.bold,
                            shadows: [
                              Shadow(
                                blurRadius: 4.0,
                                color: Colors.black38,
                                offset: Offset(2.0, 2.0),
                              ),
                            ],
                          ),
                        ),
                        const SizedBox(height: 10.0),
                        Row(
                          mainAxisAlignment: MainAxisAlignment.spaceBetween,
                          children: [
                            Text(
                              "Card holder",
                              style: TextStyle(
                                color: Colors.black,
                                fontSize: 10.0,
                              ),
                            ),
                            Text(
                              card.cardName.toUpperCase(),
                              style: const TextStyle(
                                color: Colors.black,
                                fontSize: 16.0,
                                fontWeight: FontWeight.w500,
                              ),
                            ),
                            Column(
                              crossAxisAlignment: CrossAxisAlignment.end,
                              children: [
                                Text(
                                  "Expires at",
                                  style: TextStyle(
                                    color: Colors.black,
                                    fontSize: 10.0,
                                  ),
                                ),
                                Text(
                                  "${card.expirationMonth}"
                                  " / "
                                  "${card.expirationYear}",
                                ),
                              ],
                            ),
                          ],
                        ),

                      ],
                    ),
                  ),
                ),
                Positioned(
                  top: 16,
                  right: 16,
                  child: Column(
                    children: [
                      IconButton(
                        onPressed: (){
                          showEditCardModal(context, card);
                        },
                        icon: Icon(Icons.edit, color: AppColors.darkGreen,),
                        constraints: BoxConstraints(),
                      ),
                      IconButton(
                        onPressed: (){},
                        icon: Icon(Icons.remove, color: Colors.red),
                        constraints: BoxConstraints(),
                      ),
                    ],
                  ),
                ),
              ],
            ),
          );
        }),
      ],
    );
  }
}
