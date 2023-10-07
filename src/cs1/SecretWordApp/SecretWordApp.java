
package cs1.SecretWordApp;

import  cs1.app.*;

public class SecretWordApp
{
    //drawing alphabet string as a row of circles
    void drawAlphabet( String alphabet, double radius )
    {
        double startX = radius;
        double startY = canvas.getHeight() / 4;
        
        //looping through each letter in the alphabet
        for( int count = 0; count < alphabet.length(); count = count + 1 )
        {
            char letter = alphabet.charAt( count );
            
            //drawing white circle if letter is '*'
            if( letter == '*' )
            {
                canvas.drawCircle( startX, startY, radius, "white" );
            }
            else
            {
                canvas.drawCircle( startX, startY, radius, "red" );
                canvas.drawText( startX, startY, letter, "black" );
            }
            //updating the start X position
            startX = startX + 2 * radius;
        }
    }
    
    //checking if a letter is present in the given phrase
    boolean hasLetter( String phrase, char letter )
    {
        for( int count = 0; count < phrase.length(); count = count + 1 )
        {
            if( phrase.charAt( count ) == letter )
            {
                //returning true if letter is found
                return true;
            }
        }
        //returning false if letter is not found
        return false;
    }
    
    //creating string with n number of '*'
    String makeBlankString( int n )
    {
        String blankString = "";
        
        for( int count = 0; count < n; count = count + 1 )
        {
            blankString = blankString + "*";
        }
        return blankString;
    }
    
    //replacing a letter in the phrase with new letter
    String replaceLetter( String phrase, char oldLetter, char newLetter )
    {
        String curPhrase = "";
        
        for( int count = 0; count < phrase.length(); count = count + 1 )
        {
            char letter = phrase.charAt( count );
            
            if( letter == oldLetter )
            {
                //replacing old letter with new letter
                curPhrase = curPhrase + newLetter;  
            }
            else
            {
                curPhrase = curPhrase + letter;
            }
        }
        return curPhrase;
    }
    
    //revealing the guessed letter in secret phrase
    String revealLetter( String secretPhrase, String guessedPhrase, char letter )
    {
        String newPhrase = "";
        
        for( int count = 0; count < secretPhrase.length(); count = count + 1 )
        {
            char phraseLetter = secretPhrase.charAt(count);
            
            if( phraseLetter == letter )
            {
                newPhrase = newPhrase + letter;
            }
            else
            {
                newPhrase = newPhrase + guessedPhrase.charAt(count);
            }
        }
        return newPhrase;
    }
    
    //comparing two strings
    boolean equalStrings( String str1, String str2 )
    {
        for( int count = 0; count < str1.length(); count = count + 1 )
        {
            //checking the length and returning false if the length differs
            if( str1.length() != str2.length() )
            {
                return false;
            }
            //checking each character
            else if( str1.charAt(count) != str2.charAt(count) )
            {
                return false;
            }
        }
        return true;
    }
    
    //calculating radius for different number of alphabets
    double calculateRadius( String alphabet )
    {
        int numAlphabet = alphabet.length();
        double radius = canvas.getWidth() / ( 2 * numAlphabet );
        
        return radius;
    }
    
    //drawing emoji based on number of errors
    void drawEmoji( int errors )
    {
        double screenX = canvas.getWidth() / 2;
        double screenY = canvas.getHeight() / 2;
        
        String imageName = "errors" + errors + ".png";
        canvas.drawImage( screenX, screenY, imageName );
    }
    
    void playGame()
    {
        String secretWord = canvas.getRandomColor().toUpperCase();
        //String secretWord = "SEASHELL";
        String guessWord = makeBlankString( secretWord.length() );
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        double radius = calculateRadius( alphabet );
        
        int attempts = 0;
        
        double canvasX = canvas.getWidth() / 2;
        double canvasY = canvas.getHeight() / 2; 
        
        //running the loop until the user guesses the word or runs out of attempts
        while( ( equalStrings( secretWord, guessWord ) == false ) && attempts < 6 )
        {
            canvas.clear();
            
            drawAlphabet( alphabet, radius );
            
            canvas.drawText( canvasX, canvas.getHeight() / 8, guessWord, "white|24" );
            
            drawEmoji( attempts ); 
            
            Touch touch = canvas.waitForTouch();
            double touchX = touch.getX();
            
            int guessIndex = ( int )( touchX / ( 2 * radius ) );
            
            char guess = alphabet.charAt( guessIndex );
            
            //char guess = canvas.readChar( "Pick a letter: " );
            
            alphabet = replaceLetter( alphabet, guess, '*' ); 
            
            if( guess == '*' )
            {
                canvas.playAudio( "used.mp3" );
            }
            //revealing the guessed letter and playing the 'correct' sound
            else if( hasLetter(secretWord, guess) == true )
            {
                guessWord = revealLetter( secretWord, guessWord, guess );
                canvas.playAudio( "correct.mp3" ); 
            }
            else
            {
                //increasing the attempts when the guess is wrong
                attempts = attempts + 1;
                drawEmoji( attempts);

                canvas.playAudio( "wrong.mp3" );
            }
        }
        
        //displaying winning message
        canvas.clear();
        canvas.drawText( canvasX, canvas.getHeight() / 8, "The word was " + secretWord + " !", "white|24" );
        drawEmoji( attempts );
        if( equalStrings( secretWord, guessWord )  == true )
        {
            canvas.drawText( canvasX, ( 3 * canvas.getHeight() ) / 4, " You Won! ", "white|24" );
            canvas.playAudio( "won.mp3" );
        }
        //displaying losing message
        else
        {
            canvas.drawText( canvasX, ( 3 * canvas.getHeight() ) / 4, " You Lost! ", "white|24" );
            canvas.playAudio( "lost.mp3" );
            
            canvas.sleep( 1 );
        }
    }
    
    //running the program
    public void run()
    {
        canvas.setBackground( "black" );
        
        canvas.setLandscape();
        
        //drawAlphabet( "AB*D*F", 35 );
        //drawAlphabet( "A**D*FGH**KLN*", 20 );
        //drawAlphabet( "A**D******KLN*", 12 );
        //drawAlphabet( "**CD***H**KL**", 18 );
        
        //System.out.println( "Is l in '': " + hasLetter("", 'l') );
        //System.out.println( "Is a in h: " + hasLetter("h", 'a') );
        //System.out.println( "Is l in he: " + hasLetter("he", 'l') );
        //System.out.println( "Is l in hello: " + hasLetter("hello", 'l') );
        //System.out.println( "Is d in world: " + hasLetter("world", 'd') );
        
        //System.out.println( "String with 4 stars: " + makeBlankString( 4 ) ); 
        //System.out.println( "String with 2 stars: " + makeBlankString( 2 ) );
        //System.out.println( "String with 0 stars: " + makeBlankString( 0 ) );
        //System.out.println( "String with 5 stars: " + makeBlankString( 5 ) );
        //System.out.println( "String with 8 stars: " + makeBlankString( 8 ) );
        
        //System.out.println( "New phrase is: " + replaceLetter( "kelly", 'l', 'n' ) );
        //System.out.println( "New phrase is: " + replaceLetter( "abcdef", 'd', '*' ) );
        //System.out.println( "New phrase is: " + replaceLetter( "", 'l', 'n' ) );
        //System.out.println( "New phrase is: " + replaceLetter( "k", 'k', 'n' ) );
        //System.out.println( "New phrase is: " + replaceLetter( "be", 'b', 'e' ) );
        //System.out.println( "New phrase is: " + replaceLetter( "star", 't', 'l' ) );
        
        //System.out.println( "New phrase is: " + revealLetter( "", "", 'l' ) );
        //System.out.println( "New phrase is: " + revealLetter( "h", "*", 'l' ) );
        //System.out.println( "New phrase is: " + revealLetter( "he", "**", 'e' ) );
        //System.out.println( "New phrase is: " + revealLetter( "indigo", "*n***o", 'i' ) );
        //System.out.println( "New phrase is: " + revealLetter( "parrot", "*a***t", 'p' ) );
        
        //System.out.println( "Is strings same: " + equalStrings("", "") );
        //System.out.println( "Is strings same: " + equalStrings("he", "be") );
        //System.out.println( "Is strings same: " + equalStrings("parrot", "carrot") );
        //System.out.println( "Is strings same: " + equalStrings("c", "l") );
        //System.out.println( "Is strings same: " + equalStrings("hello", "Hello") );
        //System.out.println( "Is strings same: " + equalStrings("hello", "hi") );
        //System.out.println( "Is strings same: " + equalStrings("hello", "hello") );
        
        playGame();
    }
}

