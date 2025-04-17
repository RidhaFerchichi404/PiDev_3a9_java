<?php
// Inclure les fichiers nécessaires pour la base de données et les classes
include '../../Model/user.php';
include '../../Controller/userC.php';

$message = '';

if (isset($_POST['entrer'])) {
    // Get form data
    $nom = $_POST['nom'];
    $prenom = $_POST['prenom'];
    $email = $_POST['email'];
    $adresse = $_POST['adresse'];
    $telephone = $_POST['telephone'];
    $mdp = $_POST['mot_de_passe'];
    $verification_mdp = $_POST['verification'];

    try {
        // Vérifier si l'e-mail existe déjà
        $pdo = config::getConnexion();
        $checkIfExists = "SELECT COUNT(*) as count FROM user WHERE email = ?";
        $stmtCheck = $pdo->prepare($checkIfExists);
        $stmtCheck->execute([$email]);
        $result = $stmtCheck->fetch(PDO::FETCH_ASSOC);

        if ($result['count'] > 0) {
            // Afficher un message si l'email existe
            $message = '<span style="color: red;">Cette adresse mail est déjà associée à un compte existant. Veuillez utiliser une autre adresse.</span>';
        } else {
            // Ajouter l'utilisateur avec les nouveaux champs
            $user = new User(null, $nom, $prenom, $email, password_hash($mdp, PASSWORD_DEFAULT) , 0,$adresse, $telephone);
            $userC = new UserC();
            $userC->addUser($user);

            // Succès
            echo '<script>
                    alert("Inscription réussie !");
                    window.location.href = "index.html";
                  </script>';
            exit();
        }
    } catch (Exception $e) {
        $message = '<span style="color: red;">Erreur : ' . $e->getMessage() . '</span>';
    }
}
?>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="utf-8">
    <meta content="width=device-width, initial-scale=1.0" name="viewport">
    <title>Sign Up - Create Your Account 🌟</title>

    <!-- Favicons -->
    <link href="assets/img/favicon.png" rel="icon">
    <link href="assets/img/apple-touch-icon.png" rel="apple-touch-icon">

    <!-- Google Fonts -->
    <link href="https://fonts.gstatic.com" rel="preconnect">
    <link href="https://fonts.googleapis.com/css2?family=Open+Sans:wght@300;400;600&family=Poppins:wght@300;400;600&display=swap" rel="stylesheet">

    <!-- Vendor CSS Files -->
    <link href="css/bootstrap.min.css" rel="stylesheet">
    <link href="css/bootstrap-icons.css" rel="stylesheet">

    <!-- Template Main CSS File -->
    <link href="css/style.css" rel="stylesheet">

    <style>
        /* Your existing styles */
    </style>
</head>

<body>

    <main>
        <div class="container">
            <section class="section register min-vh-100 d-flex flex-column align-items-center justify-content-center py-4">
                <div class="container">
                    <div class="row justify-content-center">
                        <div class="col-lg-4 col-md-6 d-flex flex-column align-items-center justify-content-center">

                            <div class="d-flex justify-content-center py-4">
                                <a href="index.html" class="logo d-flex align-items-center w-auto">
                                    <img src="logosignin.png" alt="logo">
                                    <span class="d-none d-lg-block">Basma</span>
                                </a>
                            </div><!-- End Logo -->

                            <div class="card mb-3">
                                <div class="card-body">
                                    <h2 class="card-title">📝 Create an Account</h2>
                                    <?php if (!empty($message)) echo $message; ?>
                                    <form id="registerForm" method="POST" novalidate autocomplete="off">
                                        <div class="form-group mb-3">
                                            <label for="name">👤 Last Name</label>
                                            <input type="text" class="form-control" id="name" name="nom" >
                                            <span id="nameError" class="error-message"></span>
                                        </div>
                                        <div class="form-group mb-3">
                                            <label for="prenom">👤 First Name</label>
                                            <input type="text" class="form-control" id="prenom" name="prenom" >
                                            <span id="prenomError" class="error-message"></span>
                                        </div>
                                        <div class="form-group mb-3">
                                            <label for="address">🏠 Address</label>
                                            <input type="text" class="form-control" id="adresse" name="adresse" >
                                            <span id="adresseError" class="error-message"></span>
                                        </div>
                                        <div class="form-group mb-3">
                                            <label for="telephone">📱 Phone Number</label>
                                            <input type="text" class="form-control" id="telephone" name="telephone" >
                                            <span id="telephoneError" class="error-message"></span>
                                        </div>
                                        <div class="form-group mb-3">
                                            <label for="email">📧 Email Address</label>
                                            <input type="email" class="form-control" id="email" name="email" >
                                            <span id="emailError" class="error-message"></span>
                                        </div>
                                        <div class="form-group mb-3">
                                            <label for="password">🔑 Password</label>
                                            <input type="password" class="form-control" id="password" name="mot_de_passe" >
                                            <span id="passwordError" class="error-message"></span>
                                        </div>
                                        <div class="form-group mb-3">
                                            <label for="password_confirmation">🔐 Confirm Password</label>
                                            <input type="password" class="form-control" id="password_confirmation" name="verification" >
                                            <span id="confirmPasswordError" class="error-message"></span>
                                        </div>
                                        <button type="submit" class="btn btn-success btn-block" name="entrer">Register</button>
                                    </form>
                                </div>
                                <div class="card-footer text-center">
                                    <p>Already have an account? <a href="signin.html" class="text-success">Login here</a></p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </section>
        </div>
    </main><!-- End #main -->

    <a href="#" class="back-to-top d-flex align-items-center justify-content-center"><i class="bi bi-arrow-up-short"></i></a>

    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script>
    document.getElementById('registerForm').addEventListener('submit', function (e) {
        let isValid = true;

        // Clear previous error messages
        document.getElementById('nameError').textContent = '';
        document.getElementById('prenomError').textContent = '';
        document.getElementById('adresseError').textContent = '';
        document.getElementById('telephoneError').textContent = '';
        document.getElementById('emailError').textContent = '';
        document.getElementById('passwordError').textContent = '';
        document.getElementById('confirmPasswordError').textContent = '';

        // Get form values
        const name = document.getElementById('name').value.trim();
        const prenom = document.getElementById('prenom').value.trim();
        const adresse = document.getElementById('adresse').value.trim();
        const telephone = document.getElementById('telephone').value.trim();
        const email = document.getElementById('email').value.trim();
        const password = document.getElementById('password').value.trim();
        const confirmPassword = document.getElementById('password_confirmation').value.trim();

        // Validate name
        if (name === '') {
            document.getElementById('nameError').textContent = 'Last name is required.';
            isValid = false;
        }

        // Validate prenom (first name)
        if (prenom === '') {
            document.getElementById('prenomError').textContent = 'First name is required.';
            isValid = false;
        }

        // Validate address
        if (adresse === '') {
            document.getElementById('adresseError').textContent = 'Address is required.';
            isValid = false;
        }

        // Validate telephone (phone number)
        if (telephone === '') {
            document.getElementById('telephoneError').textContent = 'Phone number is required.';
            isValid = false;
        }

        // Validate email
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (email === '') {
            document.getElementById('emailError').textContent = 'Email is required.';
            isValid = false;
        } else if (!emailRegex.test(email)) {
            document.getElementById('emailError').textContent = 'Invalid email format.';
            isValid = false;
        }

        // Validate password
        if (password === '') {
            document.getElementById('passwordError').textContent = 'Password is required.';
            isValid = false;
        }

        // Validate confirm password
        if (confirmPassword === '') {
            document.getElementById('confirmPasswordError').textContent = 'Please confirm your password.';
            isValid = false;
        } else if (password !== confirmPassword) {
            document.getElementById('confirmPasswordError').textContent = 'Passwords do not match.';
            isValid = false;
        }

        // Prevent form submission if validation fails
        if (!isValid) {
            e.preventDefault();
        }
    });
    </script>
</body>
</html>
