user abstract
    -> ka metode per login/logout, 

subclass per librarian, manager, administrator (nga 1 secila)
    Librarian extends user
        -> get books (read file)
        -> create bill
        -> save transaction (to file)
    Menager extends Librarian
        -> DOES ALL LIBRARIAN DOES
        -> get librarians (read file)
        -> get transactions (read file)
        -> add book (to file)
        -> query with transactions (total bills, books sold, total made by a librarian)
    Administrator extends Manager
        -> DOES ALL MANAGER AND LIBRARIAN DO
        -> add libraian (to file)
        -> get managers (read file)
        -> add manager (to file)
        -> update librarian/manager (update file)
        -> delete librarian/manager (update file)
        -> any query (total income, total cost, staff salaries)

Per GUI, secili nga user roles ka 1 menu kryesore me butona per secilen metode
    -> psh: Librarian ka ne menu options per cart, books, checkout