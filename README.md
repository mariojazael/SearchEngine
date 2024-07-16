Simple search engine

This application has the purpose of finding a given sequence into several texts, returning the ones
where the sequence were found. The application starts by reading user's arguments that contains a
file name where the input data is located. the argument has the following syntax:

| Command          | Description                                                                                                                                 |
|------------------|---------------------------------------------------------------------------------------------------------------------------------------------|
| `--data <value>` | Takes the name of the file where the input data is located. It attempts to read the file and save every line from it into a data structure. |


In case there is no --data argument from the user, the program will request the input via console.

Once the data is read, the program stores every word from every saved line into a separate map data
structure where the key is a word and the value contains the number of the lines where that word
appears. Once all of this is set, our program displays a menu that looks as follows:

=== Menu ===
1. Find a person
2. Print all persons
0. Exit

The first option allow us to search for a given key words into the records saved. For example, we can input:
"Erick Henderson" and depending on the searching strategy our program will modify its behaviour. Before
searching begins, the program will ask for the desired searching strategy. There are three:

| Strategy | Description                                                                   |
|----------|-------------------------------------------------------------------------------|
| `ALL`    | The program prints lines containing all the words from the query.             |
| `ANY`    | The program prints lines containing at least one word from the query.         |
| `NONE`   | The program prints lines that do not contain any of the words from the query. |

Once this is set, the program searches according to it and then prints out the records that match. All of
the searching operations performed by this application are helped out by the map filled at the beginning
of the application.

The second option simply displays all the records.

The third option terminates the program's execution.
