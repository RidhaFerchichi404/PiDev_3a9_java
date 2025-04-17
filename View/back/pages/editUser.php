<?php
include_once 'c:/xampp/htdocs/projetuser/Controller/userC.php';
$userC = new userC();

if (isset($_GET['id'])) {
    $userId = $_GET['id'];

    // Récupération des informations de l'utilisateur
    $user = $userC->listUsersById($userId);

    if (!$user) {
        // Si aucun utilisateur n'est trouvé, redirection
        header('Location: error.php');
        exit();
    }

    // Si le formulaire est soumis
    if ($_SERVER["REQUEST_METHOD"] == "POST") {
        $nom = $_POST["nom"];
        $prenom = $_POST["prenom"]; // Nouveau champ
        $email = $_POST["email"];
        $mdp = $_POST["mdp"];
        $role = $_POST["role"];
        $adresse = $_POST["adresse"]; // Nouveau champ
        $telephone = $_POST["telephone"]; // Nouveau champ

        try {
            $user = new User($userId, $nom, $prenom, $email, $mdp, $role, $adresse, $telephone);
            $userC->updateUser($userId, $user);

            header('Location: Dashboard.php');
            exit();
        } catch (PDOException $e) {
            echo "Erreur: " . $e->getMessage();
        }
    }
} else {
    header('Location: index.php');
    exit();
}
?>

<!DOCTYPE html>
<html lang="en">

<head>
  <meta charset="utf-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
  <link rel="apple-touch-icon" sizes="76x76" href="../assets/img/apple-icon.png">
  <link rel="icon" type="image/png" href="../assets/img/favicon.png">
  <title>
    Material Dashboard 3 by Creative Tim
  </title>
  <!--     Fonts and icons     -->
  <link rel="stylesheet" type="text/css" href="https://fonts.googleapis.com/css?family=Inter:300,400,500,600,700,900" />
  <!-- Nucleo Icons -->
  <link href="../assets/css/nucleo-icons.css" rel="stylesheet" />
  <link href="../assets/css/nucleo-svg.css" rel="stylesheet" />
  <!-- Font Awesome Icons -->
  <script src="https://kit.fontawesome.com/42d5adcbca.js" crossorigin="anonymous"></script>
  <!-- Material Icons -->
  <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Material+Symbols+Rounded:opsz,wght,FILL,GRAD@24,400,0,0" />
  <!-- CSS Files -->
  <link id="pagestyle" href="../assets/css/material-dashboard.css?v=3.2.0" rel="stylesheet" />
</head>

<body class="g-sidenav-show  bg-gray-100">
  <aside class="sidenav navbar navbar-vertical navbar-expand-xs border-radius-lg fixed-start ms-2  bg-white my-2" id="sidenav-main">
    <div class="sidenav-header">
      <i class="fas fa-times p-3 cursor-pointer text-dark opacity-5 position-absolute end-0 top-0 d-none d-xl-none" aria-hidden="true" id="iconSidenav"></i>
      <a class="navbar-brand px-4 py-3 m-0" href=" https://demos.creative-tim.com/material-dashboard/pages/dashboard " target="_blank">
        <img src="../assets/img/logo-ct-dark.png" class="navbar-brand-img" width="26" height="26" alt="main_logo">
        <span class="ms-1 text-sm text-dark">Creative Tim</span>
      </a>
    </div>
    <hr class="horizontal dark mt-0 mb-2">
    <div class="collapse navbar-collapse  w-auto " id="sidenav-collapse-main">
      <ul class="navbar-nav">
        <li class="nav-item">
          <a class="nav-link active bg-gradient-dark text-white" href="../pages/dashboard.html">
            <i class="material-symbols-rounded opacity-5">dashboard</i>
            <span class="nav-link-text ms-1">Dashboard</span>
          </a>
        </li>
        
      
       
       
        <li class="nav-item">
          <a class="nav-link text-dark" href="../pages/notifications.html">
            <i class="material-symbols-rounded opacity-5">notifications</i>
            <span class="nav-link-text ms-1">Notifications</span>
          </a>
        </li>
    
     

       
      </ul>
    </div>
    <div class="sidenav-footer position-absolute w-100 bottom-0 ">
      <div class="mx-3">
        <a class="btn btn-outline-dark mt-4 w-100" href="https://www.creative-tim.com/learning-lab/bootstrap/overview/material-dashboard?ref=sidebarfree" type="button">Documentation</a>
        <a class="btn bg-gradient-dark w-100" href="https://www.creative-tim.com/product/material-dashboard-pro?ref=sidebarfree" type="button">Upgrade to pro</a>
      </div>
    </div>
  </aside>
  <main class="main-content position-relative max-height-vh-100 h-100 border-radius-lg ">
    <!-- Navbar -->

    <nav class="navbar navbar-main navbar-expand-lg px-0 mx-3 shadow-none border-radius-xl" id="navbarBlur" data-scroll="true">
      <div class="container-fluid py-1 px-3">
        <nav aria-label="breadcrumb">
          <ol class="breadcrumb bg-transparent mb-0 pb-0 pt-1 px-0 me-sm-6 me-5">
            <li class="breadcrumb-item text-sm"><a class="opacity-5 text-dark" href="javascript:;">Pages</a></li>
            <li class="breadcrumb-item text-sm text-dark active" aria-current="page">Tables</li>
          </ol>
        </nav>
        <div class="collapse navbar-collapse mt-sm-0 mt-2 me-md-0 me-sm-4" id="navbar">
          <div class="ms-md-auto pe-md-3 d-flex align-items-center">
            <div class="input-group input-group-outline">
              <label class="form-label">Type here...</label>
              <input type="text" class="form-control">
            </div>
          </div>
          <ul class="navbar-nav d-flex align-items-center  justify-content-end">
            <li class="nav-item d-flex align-items-center">
              <a class="btn btn-outline-primary btn-sm mb-0 me-3" target="_blank" href="https://www.creative-tim.com/builder?ref=navbar-material-dashboard">Online Builder</a>
            </li>
            <li class="mt-1">
              <a class="github-button" href="https://github.com/creativetimofficial/material-dashboard" data-icon="octicon-star" data-size="large" data-show-count="true" aria-label="Star creativetimofficial/material-dashboard on GitHub">Star</a>
            </li>
            <li class="nav-item d-xl-none ps-3 d-flex align-items-center">
              <a href="javascript:;" class="nav-link text-body p-0" id="iconNavbarSidenav">
                <div class="sidenav-toggler-inner">
                  <i class="sidenav-toggler-line"></i>
                  <i class="sidenav-toggler-line"></i>
                  <i class="sidenav-toggler-line"></i>
                </div>
              </a>
            </li>
            <li class="nav-item px-3 d-flex align-items-center">
              <a href="javascript:;" class="nav-link text-body p-0">
                <i class="material-symbols-rounded fixed-plugin-button-nav">settings</i>
              </a>
            </li>
            <li class="nav-item dropdown pe-3 d-flex align-items-center">
              <a href="javascript:;" class="nav-link text-body p-0" id="dropdownMenuButton" data-bs-toggle="dropdown" aria-expanded="false">
                <i class="material-symbols-rounded">notifications</i>
              </a>
              <ul class="dropdown-menu  dropdown-menu-end  px-2 py-3 me-sm-n4" aria-labelledby="dropdownMenuButton">
                <li class="mb-2">
                  <a class="dropdown-item border-radius-md" href="javascript:;">
                    <div class="d-flex py-1">
                      <div class="my-auto">
                        <img src="../assets/img/team-2.jpg" class="avatar avatar-sm  me-3 ">
                      </div>
                      <div class="d-flex flex-column justify-content-center">
                        <h6 class="text-sm font-weight-normal mb-1">
                          <span class="font-weight-bold">New message</span> from Laur
                        </h6>
                        <p class="text-xs text-secondary mb-0">
                          <i class="fa fa-clock me-1"></i>
                          13 minutes ago
                        </p>
                      </div>
                    </div>
                  </a>
                </li>
                <li class="mb-2">
                  <a class="dropdown-item border-radius-md" href="javascript:;">
                    <div class="d-flex py-1">
                      <div class="my-auto">
                        <img src="../assets/img/small-logos/logo-spotify.svg" class="avatar avatar-sm bg-gradient-dark  me-3 ">
                      </div>
                      <div class="d-flex flex-column justify-content-center">
                        <h6 class="text-sm font-weight-normal mb-1">
                          <span class="font-weight-bold">New album</span> by Travis Scott
                        </h6>
                        <p class="text-xs text-secondary mb-0">
                          <i class="fa fa-clock me-1"></i>
                          1 day
                        </p>
                      </div>
                    </div>
                  </a>
                </li>
                <li>
                  <a class="dropdown-item border-radius-md" href="javascript:;">
                    <div class="d-flex py-1">
                      <div class="avatar avatar-sm bg-gradient-secondary  me-3  my-auto">
                        <svg width="12px" height="12px" viewBox="0 0 43 36" version="1.1" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink">
                          <title>credit-card</title>
                          <g stroke="none" stroke-width="1" fill="none" fill-rule="evenodd">
                            <g transform="translate(-2169.000000, -745.000000)" fill="#FFFFFF" fill-rule="nonzero">
                              <g transform="translate(1716.000000, 291.000000)">
                                <g transform="translate(453.000000, 454.000000)">
                                  <path class="color-background" d="M43,10.7482083 L43,3.58333333 C43,1.60354167 41.3964583,0 39.4166667,0 L3.58333333,0 C1.60354167,0 0,1.60354167 0,3.58333333 L0,10.7482083 L43,10.7482083 Z" opacity="0.593633743"></path>
                                  <path class="color-background" d="M0,16.125 L0,32.25 C0,34.2297917 1.60354167,35.8333333 3.58333333,35.8333333 L39.4166667,35.8333333 C41.3964583,35.8333333 43,34.2297917 43,32.25 L43,16.125 L0,16.125 Z M19.7083333,26.875 L7.16666667,26.875 L7.16666667,23.2916667 L19.7083333,23.2916667 L19.7083333,26.875 Z M35.8333333,26.875 L28.6666667,26.875 L28.6666667,23.2916667 L35.8333333,23.2916667 L35.8333333,26.875 Z"></path>
                                </g>
                              </g>
                            </g>
                          </g>
                        </svg>
                      </div>
                      <div class="d-flex flex-column justify-content-center">
                        <h6 class="text-sm font-weight-normal mb-1">
                          Payment successfully completed
                        </h6>
                        <p class="text-xs text-secondary mb-0">
                          <i class="fa fa-clock me-1"></i>
                          2 days
                        </p>
                      </div>
                    </div>
                  </a>
                </li>
              </ul>
            </li>
            <li class="nav-item d-flex align-items-center">
              <a href="../pages/sign-in.html" class="nav-link text-body font-weight-bold px-0">
                <i class="material-symbols-rounded">account_circle</i>
              </a>
            </li>
          </ul>
        </div>
      </div>
    </nav>

      <!-- Main Content -->
      <div class="main-panel">
    <div class="content">
        <div class="form-container">
            <h1 class="form-title">🛠️ Modifier Utilisateur</h1>
            <form id="editForm" method="POST">
                <div class="form-group">
                    <label for="nom">Nom</label>
                    <input type="text" id="nom" name="nom" value="<?php echo $user->getNom(); ?>" required>
                    <div id="nameError" class="error-message"></div>
                </div>
                <div class="form-group">
                    <label for="prenom">Prénom</label>
                    <input type="text" id="prenom" name="prenom" value="<?php echo $user->getPrenom(); ?>" required>
                    <div id="prenomError" class="error-message"></div>
                </div>
                <div class="form-group">
                    <label for="email">Email</label>
                    <input type="email" id="email" name="email" value="<?php echo $user->getEmail(); ?>" required>
                    <div id="emailError" class="error-message"></div>
                </div>
                <div class="form-group">
                    <label for="mdp">Mot de passe</label>
                    <input type="password" id="mdp" name="mdp" value="<?php echo $user->getMdp(); ?>" required>
                    <div id="passwordError" class="error-message"></div>
                </div>
                <div class="form-group">
                    <label for="role">Rôle</label>
                    <select id="role" name="role">
                        <option value="0" <?php echo $user->getRole() == 0 ? 'selected' : ''; ?>>Utilisateur</option>
                        <option value="1" <?php echo $user->getRole() == 1 ? 'selected' : ''; ?>>Administrateur</option>
                    </select>
                    <div id="roleError" class="error-message"></div>
                </div>
                <div class="form-group">
                    <label for="adresse">Adresse</label>
                    <input type="text" id="adresse" name="adresse" value="<?php echo $user->getAdresse(); ?>" required>
                    <div id="adresseError" class="error-message"></div>
                </div>
                <div class="form-group">
                    <label for="telephone">Téléphone</label>
                    <input type="text" id="telephone" name="telephone" value="<?php echo $user->getTelephone(); ?>" required>
                    <div id="telephoneError" class="error-message"></div>
                </div>
                <div class="form-group">
                    <button type="submit" class="btn-submit">💾 Modifier</button>
                </div>
            </form>
        </div>
    </div>
</div>
<style>
  .content {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100vh;
    background-color: #f9f9f9;
}

.form-container {
    background-color: white;
    padding: 30px;
    border-radius: 8px;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
    width: 100%;
    max-width: 500px;
}

.form-title {
    text-align: center;
    font-size: 24px;
    margin-bottom: 20px;
    color: #333;
    font-weight: bold;
}

.form-group {
    margin-bottom: 15px;
}

.form-group label {
    display: block;
    margin-bottom: 5px;
    font-weight: bold;
    color: #555;
}

.form-group input,
.form-group select {
    width: 100%;
    padding: 10px;
    border: 1px solid #ddd;
    border-radius: 4px;
    font-size: 14px;
}

.form-group input:focus,
.form-group select:focus {
    border-color: #4CAF50;
    outline: none;
    box-shadow: 0 0 5px rgba(76, 175, 80, 0.5);
}

.error-message {
    color: red;
    font-size: 12px;
    margin-top: 5px;
}

.btn-submit {
    width: 100%;
    padding: 12px;
    background-color: #4CAF50;
    color: white;
    font-size: 16px;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    font-weight: bold;
}

.btn-submit:hover {
    background-color: #45a049;
}
</style>

    <script>
        document.getElementById('editForm').addEventListener('submit', function(e) {
            let isValid = true;

            // Effacer les messages d'erreur précédents
            document.getElementById('nameError').textContent = '';
            document.getElementById('prenomError').textContent = '';
            document.getElementById('emailError').textContent = '';
            document.getElementById('passwordError').textContent = '';
            document.getElementById('roleError').textContent = '';
            document.getElementById('adresseError').textContent = '';
            document.getElementById('telephoneError').textContent = '';

            // Récupérer les valeurs du formulaire
            const name = document.getElementById('nom').value.trim();
            const prenom = document.getElementById('prenom').value.trim();
            const email = document.getElementById('email').value.trim();
            const password = document.getElementById('mdp').value.trim();
            const role = document.getElementById('role').value;
            const adresse = document.getElementById('adresse').value.trim();
            const telephone = document.getElementById('telephone').value.trim();

            // Validation des champs
            if (name === '') {
                document.getElementById('nameError').textContent = 'Le nom est requis.';
                isValid = false;
            }
            if (prenom === '') {
                document.getElementById('prenomError').textContent = 'Le prénom est requis.';
                isValid = false;
            }
            const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            if (email === '') {
                document.getElementById('emailError').textContent = 'L\'email est requis.';
                isValid = false;
            } else if (!emailRegex.test(email)) {
                document.getElementById('emailError').textContent = 'Format d\'email invalide.';
                isValid = false;
            }
            if (password === '') {
                document.getElementById('passwordError').textContent = 'Le mot de passe est requis.';
                isValid = false;
            }
            if (role !== '0' && role !== '1') {
                document.getElementById('roleError').textContent = 'Rôle invalide.';
                isValid = false;
            }
            if (adresse === '') {
                document.getElementById('adresseError').textContent = 'L\'adresse est requise.';
                isValid = false;
            }
            if (telephone === '') {
                document.getElementById('telephoneError').textContent = 'Le téléphone est requis.';
                isValid = false;
            }

            // Annuler la soumission si des erreurs existent
            if (!isValid) {
                e.preventDefault();
            }
        });
    </script>
  </body>
</html>

